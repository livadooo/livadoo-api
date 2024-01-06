package com.livadoo.services.account

import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.ProxyLanguage
import com.livadoo.proxy.otp.OtpServiceProxy
import com.livadoo.proxy.otp.ProxyOtpType
import com.livadoo.proxy.user.UserServiceProxy
import com.livadoo.utils.exception.InternalErrorException
import com.livadoo.utils.security.config.AppSecurityContext
import com.livadoo.utils.security.domain.SYSTEM_ACCOUNT
import com.livadoo.utils.user.UserDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class DefaultAccountService(
    private val userServiceProxy: UserServiceProxy,
    private val securityContext: AppSecurityContext,
    private val notificationServiceProxy: NotificationServiceProxy,
    private val otpServiceProxy: OtpServiceProxy,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val clock: Clock,
) : AccountService {
    override suspend fun getCurrentUser(): UserDto {
        return userServiceProxy.getUserById(securityContext.getCurrentUserId())
    }

    override suspend fun requestPasswordReset(resetRequest: PasswordResetRequest) {
        try {
            val userDto = userServiceProxy.getUserByEmail(resetRequest.email)
            if (userDto.activated && !userDto.blocked && !userDto.deleted) {
                val resetLink = otpServiceProxy.createOtp(resetRequest.email, ProxyOtpType.STAFF_PASSWORD_RESET)
                notificationServiceProxy.notifyPasswordResetRequest(
                    firstName = userDto.firstName!!,
                    email = userDto.email!!,
                    resetLink = resetLink,
                    language = ProxyLanguage.valueOf(userDto.language.name),
                )
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun checkStaffOtpValidity(otp: String): Boolean {
        return otpServiceProxy.isOtpValid(otp = otp, otpType = ProxyOtpType.STAFF_PASSWORD_RESET)
    }

    override suspend fun resetPassword(passwordReset: PasswordReset) {
        otpServiceProxy.validateOtp(otp = passwordReset.otp, otpType = ProxyOtpType.STAFF_PASSWORD_RESET)
            ?.let { email ->
                val userEntity = accountRepository.findByEmailIgnoreCase(email)
                    ?: throw InternalErrorException()
                userEntity.apply {
                    password = passwordEncoder.encode(passwordReset.password)
                    updatedAt = clock.instant()
                    updatedBy = SYSTEM_ACCOUNT
                }
                accountRepository.save(userEntity)
                notificationServiceProxy.notifyPasswordReset(
                    firstName = userEntity.firstName!!,
                    email = userEntity.email!!,
                    language = ProxyLanguage.valueOf(userEntity.language.name),
                )
            } ?: throw InvalidPasswordResetOtpException(passwordReset.otp)
    }
}
