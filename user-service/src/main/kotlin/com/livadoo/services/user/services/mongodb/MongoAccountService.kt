package com.livadoo.services.user.services.mongodb

import com.livadoo.library.security.domain.AuthUser
import com.livadoo.library.security.domain.SYSTEM_ACCOUNT
import com.livadoo.library.security.utils.currentUserId
import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.model.PasswordResetRequest
import com.livadoo.services.user.config.PasswordResetKeyProperties
import com.livadoo.services.user.data.PasswordReset
import com.livadoo.services.user.data.User
import com.livadoo.services.user.exceptions.SecureKeyExpiredException
import com.livadoo.services.user.exceptions.SecureKeyNotFoundException
import com.livadoo.services.user.exceptions.UserNotFoundException
import com.livadoo.services.user.exceptions.WrongCredentialsException
import com.livadoo.services.user.extensions.saveOrUpdate
import com.livadoo.services.user.security.AuthUserDTO
import com.livadoo.services.user.security.JwtSigner
import com.livadoo.services.user.security.LoginCredentials
import com.livadoo.services.user.services.AccountService
import com.livadoo.services.user.services.mongodb.entity.KeyType.PASSWORD_RESET
import com.livadoo.services.user.services.mongodb.entity.SecureKeyEntity
import com.livadoo.services.user.services.mongodb.entity.UserEntity
import com.livadoo.services.user.services.mongodb.entity.toDto
import com.livadoo.services.user.services.mongodb.repository.SecureKeyRepository
import com.livadoo.services.user.services.mongodb.repository.UserRepository
import com.livadoo.utils.exception.UnauthorizedException
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random
import kotlin.random.nextInt
import com.livadoo.services.user.data.PasswordResetRequest as InternalPasswordResetRequest

@Service
class MongoAccountService(
    private val userRepository: UserRepository,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val jwtSigner: JwtSigner,
    private val passwordEncoder: PasswordEncoder,
    private val passwordResetKeyProperties: PasswordResetKeyProperties,
    private val secureKeyRepository: SecureKeyRepository,
    private val notificationService: NotificationServiceProxy,
) : AccountService {
    private val logger = LoggerFactory.getLogger(MongoAccountService::class.simpleName)

    override suspend fun login(credentials: LoginCredentials): AuthUserDTO {
        return try {
            val authentication =
                authenticationManager
                    .authenticate(UsernamePasswordAuthenticationToken(credentials.email, credentials.password))
                    .awaitFirst()
            signToken(authentication)
        } catch (exception: BadCredentialsException) {
            throw WrongCredentialsException()
        }
    }

    override suspend fun refreshToken(refreshToken: String): AuthUserDTO {
        val userId = jwtSigner.getUserId(refreshToken) ?: throw UnauthorizedException("Invalid or expired token")
        val user =
            userRepository.findById(userId).awaitSingleOrNull()
                ?: throw UnauthorizedException("Invalid or expired token")
        return internalAuthenticate(user)
    }

    override suspend fun loginAfterAccountActivation(email: String): AuthUserDTO {
        val user = userRepository.findByEmailIgnoreCase(email).awaitSingle()
        return internalAuthenticate(user)
    }

    override suspend fun getCurrentUser(): User {
        return currentUserId.awaitFirstOrNull()
            ?.let { userId -> userRepository.findById(userId).awaitSingleOrNull() }
            ?.toDto()
            ?: throw UnauthorizedException("You are not authenticated")
    }

    override suspend fun requestPasswordReset(passwordResetRequest: InternalPasswordResetRequest) {
        userRepository.findByEmailIgnoreCase(passwordResetRequest.email)
            .awaitSingleOrNull()
            ?.also { entity ->
                if (entity.verified && !entity.blocked) {
                    val userId = entity.id!!
                    val key = Random.nextInt(IntRange(100000, 999999)).toString()
                    val keyValidityTime = passwordResetKeyProperties.validityTimeInHours
                    val expiresAt = Instant.now().plus(keyValidityTime.toLong(), ChronoUnit.HOURS)
                    val resetKeyEntity = SecureKeyEntity(key, userId, PASSWORD_RESET, expiresAt, Instant.now(), Instant.now())

                    resetKeyEntity.saveOrUpdate(secureKeyRepository, userId, key, expiresAt)

                    val resetRequest = PasswordResetRequest(userId, entity.email, key, keyValidityTime)
                    mono {
                        notificationService.sendUserPasswordResetRequestEmail(resetRequest)
                    }.subscribe { logger.info("Email de réinitialisation du mot de passe envoyé avec succès.") }
                }
            }
    }

    override suspend fun resetPassword(passwordReset: PasswordReset) {
        val resetKey =
            secureKeyRepository.findByKey(passwordReset.resetKey).awaitSingleOrNull()
                ?: throw SecureKeyNotFoundException(passwordReset.resetKey)

        resetKey.takeIf { it.expirationDate >= Instant.now() }
            ?: throw SecureKeyExpiredException(passwordReset.resetKey)

        userRepository.findById(resetKey.userId).awaitSingleOrNull()
            ?.apply {
                val newPassword = passwordEncoder.encode(passwordReset.newPassword)
                password = newPassword
                updatedAt = Instant.now()
                updatedBy = currentUserId.awaitFirstOrDefault(SYSTEM_ACCOUNT)
            }
            ?.also {
                userRepository.save(it).awaitSingle()
                secureKeyRepository.delete(resetKey).awaitSingleOrNull()
            }
            ?: throw UserNotFoundException(resetKey.userId)
    }

    private fun signToken(authentication: Authentication): AuthUserDTO {
        val accessToken = jwtSigner.createAccessToken(authentication)
        val refreshToken = jwtSigner.createRefreshToken(authentication)

        return AuthUserDTO(accessToken, refreshToken)
    }

    private fun internalAuthenticate(user: UserEntity): AuthUserDTO {
        val grantedAuthority = SimpleGrantedAuthority(user.authority)
        val authUser = AuthUser(user.id!!, "", listOf(grantedAuthority))
        val authentication = UsernamePasswordAuthenticationToken(authUser, null)

        return signToken(authentication)
    }
}
