package com.livadoo.services.account

import com.livadoo.utils.user.UserDto
import org.springframework.stereotype.Service

@Service
class DefaultAccountService(
    private val accountRepository: AccountRepository,
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
}
