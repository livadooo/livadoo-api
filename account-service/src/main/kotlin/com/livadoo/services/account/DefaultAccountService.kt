package com.livadoo.services.account

import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.ProxyLanguage
import com.livadoo.proxy.otp.OtpServiceProxy
import com.livadoo.proxy.otp.ProxyOtpType
import com.livadoo.proxy.user.UserServiceProxy
import com.livadoo.utils.security.config.AppSecurityContext
import com.livadoo.utils.user.UserDto
import org.springframework.stereotype.Service

@Service
class DefaultAccountService(
    private val userServiceProxy: UserServiceProxy,
    private val securityContext: AppSecurityContext,
    private val notificationServiceProxy: NotificationServiceProxy,
    private val otpServiceProxy: OtpServiceProxy,
) : AccountService {
    override suspend fun getCurrentUser(): UserDto {
        return userServiceProxy.getUserById(securityContext.getCurrentUserId())
    }

    override suspend fun requestPasswordReset(resetRequest: PasswordResetRequest) {
        try {
            val userDto = userServiceProxy.getUserByEmail(resetRequest.email)
            if (userDto.activated && !userDto.blocked && !userDto.deleted) {
                val otp = otpServiceProxy.createOtp(resetRequest.email, ProxyOtpType.STAFF_PASSWORD_RESET)

                notificationServiceProxy.notifyPasswordResetRequest(
                    firstName = userDto.firstName!!,
                    email = userDto.email!!,
                    otp = otp,
                    language = ProxyLanguage.valueOf(userDto.language.name),
                )
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun resetPassword(passwordReset: PasswordReset) {
        TODO("Not yet implemented")
    }

    /*override suspend fun requestPasswordReset(resetRequest: PasswordResetRequest) {
        val userDto = userServiceProxy.getUserByEmail(resetRequest.email())
        val expirationTimeInHours: Int = passwordResetProperties.expirationTimeInHours()
        val expiresAt =
            Instant.now().plus(expirationTimeInHours.toLong(), ChronoUnit.HOURS)
        val resetKey: String =
            oneTimePasswordService.createOneTimePassword(PASSWORD_RESET, expiresAt, owner, SYSTEM_ACCOUNT)
        if (owner.isActivated() && !owner.isBlocked() && !owner.isDeleted()) {
            val passwordResetRequest = ResetPasswordRequest(
                owner.getId().toString(),
                owner.getFirstName(),
                owner.getEmail(),
                resetKey,
                expirationTimeInHours,
                owner.getLanguage().name()
            )
            notificationService.sendPasswordResetRequestEmail(passwordResetRequest)
        }
    }

    fun resetPassword(passwordReset: PasswordReset) {
        try {
            val oneTimePassword: OneTimePasswordDTO =
                oneTimePasswordService.getOneTimePassword(passwordReset.resetKey(), PASSWORD_RESET)
            val newEncodedPassword: String = passwordEncoder.encode(passwordReset.newPassword())
            val userEntity: UserEntity = oneTimePassword.getOwner()
            userEntity.setPassword(newEncodedPassword)
            userEntity.setUpdatedAt(Instant.now())
            userEntity.setUpdatedBy(SYSTEM_ACCOUNT)
            userRepository.save(userEntity)
            oneTimePasswordService.deleteOneTimePassword(passwordReset.resetKey())
            val accountPasswordReset = AccountPasswordReset(
                userEntity.getId().toString(),
                userEntity.getFirstName(),
                userEntity.getEmail(),
                userEntity.getLanguage().name()
            )
            notificationService.sendPasswordResetEmail(accountPasswordReset)
        } catch (exception: InvalidOrExpiredOTPException) {
            throw InvalidPasswordResetKeyException(passwordReset.resetKey())
        }
    }*/
}
