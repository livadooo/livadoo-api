package com.livadoo.services.auth

import com.livadoo.proxy.authority.search.AuthoritySearchServiceProxy
import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.ProxyLanguage.FR
import com.livadoo.proxy.otp.OtpServiceProxy
import com.livadoo.proxy.otp.ProxyOtpType.CUSTOMER_AUTH
import com.livadoo.proxy.phone.validation.PhoneValidationServiceProxy
import com.livadoo.proxy.user.UserServiceProxy
import com.livadoo.services.auth.security.JwtSigner
import com.livadoo.shared.extension.containsExceptionKey
import com.livadoo.utils.exception.InternalErrorException
import com.livadoo.utils.exception.UnauthorizedException
import com.livadoo.utils.security.domain.AuthUser
import com.livadoo.utils.user.AuthUserDto
import com.livadoo.utils.user.Language
import com.livadoo.utils.user.UserEntity
import com.livadoo.utils.user.buildUserId
import com.livadoo.utils.user.toDto
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class DefaultAuthService(
    private val jwtSigner: JwtSigner,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val userServiceProxy: UserServiceProxy,
    private val phoneValidationServiceProxy: PhoneValidationServiceProxy,
    private val authRepository: AuthRepository,
    private val otpServiceProxy: OtpServiceProxy,
    private val notificationServiceProxy: NotificationServiceProxy,
    private val authoritySearchServiceProxy: AuthoritySearchServiceProxy,
) : AuthService {
    override suspend fun requestCustomerAuth(authRequest: CustomerAuthRequest) {
        val phoneNumber = phoneValidationServiceProxy.validate(
            phoneNumber = authRequest.phoneNumber,
            regionCode = authRequest.regionCode,
        )

        val otp = otpServiceProxy.createOtp(subject = phoneNumber, otpType = CUSTOMER_AUTH)
        notificationServiceProxy.sendCustomerOtp(phoneNumber = phoneNumber, otp = otp, language = FR)
    }

    override suspend fun verifyCustomerAuth(authVerify: CustomerAuthVerify): AuthUserDto {
        val phoneNumber = phoneValidationServiceProxy.validate(
            phoneNumber = authVerify.phoneNumber,
            regionCode = authVerify.regionCode,
        )
        val isOtpValid = otpServiceProxy.isOtpValid(
            subject = phoneNumber,
            password = authVerify.otp,
            otpType = CUSTOMER_AUTH,
        )
        if (isOtpValid) {
            val userDto = try {
                userServiceProxy.getUserByPhoneNumber(phoneNumber)
            } catch (exception: Exception) {
                val userEntity = UserEntity(
                    language = Language.FR,
                    userId = buildUserId,
                    activated = false,
                    phoneNumber = phoneNumber,
                    roleIds = emptyList(),
                    permissionIds = emptyList(),
                    firstName = null,
                    lastName = null,
                    email = null,
                    password = null,
                )
                saveUser(userEntity).toDto(roles = emptyList(), permissions = emptyList())
            }
            return authenticateInternally(
                userId = userDto.userId,
                roles = userDto.roles,
                permissions = userDto.permissions,
            )
        } else {
            throw InvalidOtpException(authVerify.otp)
        }
    }

    override suspend fun authenticateStaff(credentials: StaffAuthCredentials): AuthUserDto {
        return try {
            val authentication =
                authenticationManager
                    .authenticate(UsernamePasswordAuthenticationToken(credentials.email, credentials.password))
                    .awaitFirst()
            signToken(authentication)
        } catch (exception: BadCredentialsException) {
            throw UnauthorizedException("Incorrect email or password")
        }
    }

    override suspend fun refreshToken(refreshToken: RefreshToken): AuthUserDto {
        val userId = jwtSigner.getUserId(refreshToken.token) ?: throw UnauthorizedException()
        val userDto = userServiceProxy.getUserById(userId)
        val authorities = authoritySearchServiceProxy.getAuthoritiesByUserId(userId)
        return authenticateInternally(
            userId = userDto.userId,
            permissions = authorities.permissions,
            roles = authorities.roles,
        )
    }

    private suspend fun authenticateInternally(
        userId: String,
        permissions: List<String>,
        roles: List<String>,
    ): AuthUserDto {
        val grantedPermissions = permissions.map { SimpleGrantedAuthority(it) }
        val authUser =
            AuthUser(
                username = userId,
                password = "",
                roles = roles,
                permissions = grantedPermissions,
            )
        val authentication = UsernamePasswordAuthenticationToken(authUser, null)
        return signToken(authentication)
    }

    private suspend fun saveUser(userEntity: UserEntity): UserEntity {
        return try {
            authRepository.save(userEntity)
        } catch (exception: DuplicateKeyException) {
            if (exception.message!!.containsExceptionKey("userId")) {
                saveUser(userEntity.copy(userId = buildUserId))
            } else {
                throw InternalErrorException()
            }
        }
    }

    private suspend fun signToken(authentication: Authentication): AuthUserDto {
        val accessToken = jwtSigner.createAccessToken(authentication)
        val refreshToken = jwtSigner.createRefreshToken(authentication)
        val authUser = authentication.principal as AuthUser
        val userDto = userServiceProxy.getUserById(authUser.username)
        return AuthUserDto(accessToken = accessToken, refreshToken = refreshToken, user = userDto)
    }
}
