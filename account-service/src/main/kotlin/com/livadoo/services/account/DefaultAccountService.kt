package com.livadoo.services.account

import com.livadoo.proxy.user.UserServiceProxy
import com.livadoo.utils.user.UserDto
import org.springframework.stereotype.Service

@Service
class DefaultAccountService(
    private val userServiceProxy: UserServiceProxy,
) : AccountService {
    override suspend fun getCurrentUser(): UserDto {
        TODO("Not yet implemented")
    }

    override suspend fun requestPasswordReset(resetRequest: PasswordResetRequest) {
        TODO("Not yet implemented")
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
    }

    fun updatePassword(passwordUpdate: PasswordUpdate) {
        val currentUserId = UUID.fromString(getCurrentUserId())
        val userEntity: UserEntity = userRepository.findById(currentUserId).orElseThrow()
        if (!passwordEncoder.matches(passwordUpdate.oldPassword(), userEntity.getPassword())) {
            throw WrongPasswordException()
        }
        if (passwordEncoder.matches(passwordUpdate.newPassword(), userEntity.getPassword())) {
            throw SimilarPasswordsException()
        }
        val encryptedPassword: String = passwordEncoder.encode(passwordUpdate.newPassword())
        userEntity.setPassword(encryptedPassword)
        userRepository.save(userEntity)
    }*/
}
