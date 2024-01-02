package com.livadoo.services.auth

import com.livadoo.proxy.user.UserServiceProxy
import com.livadoo.services.auth.security.JwtSigner
import com.livadoo.utils.exception.UnauthorizedException
import com.livadoo.utils.security.domain.AuthUser
import com.livadoo.utils.user.AuthUserDto
import com.livadoo.utils.user.UserDto
import kotlinx.coroutines.reactive.awaitFirst
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
) : AuthService {
    override suspend fun requestCustomerAuth(authRequest: CustomerAuthRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun verifyCustomerAuth(authVerify: CustomerAuthVerify): AuthUserDto {
        TODO("Not yet implemented")
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
        return authenticateInternally(userDto)
    }

    private suspend fun authenticateInternally(userDto: UserDto): AuthUserDto {
        val permissions = userDto.permissions.map { SimpleGrantedAuthority(it) }
        val roles = userDto.roles
        val authUser =
            AuthUser(
                username = userDto.userId,
                password = "",
                roles = roles,
                permissions = permissions,
            )
        val authentication = UsernamePasswordAuthenticationToken(authUser, null)
        return signToken(authentication)
    }

    private suspend fun signToken(authentication: Authentication): AuthUserDto {
        val accessToken = jwtSigner.createAccessToken(authentication)
        val refreshToken = jwtSigner.createRefreshToken(authentication)
        val authUser = authentication.principal as AuthUser
        val userDto = userServiceProxy.getUserById(authUser.username)
        return AuthUserDto(accessToken = accessToken, refreshToken = refreshToken, user = userDto)
    }
}
