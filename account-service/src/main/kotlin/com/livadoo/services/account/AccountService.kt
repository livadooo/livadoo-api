package com.livadoo.services.account

import com.livadoo.utils.user.UserDto

interface AccountService {
    suspend fun getCurrentUser(): UserDto

    suspend fun requestPasswordReset(resetRequest: PasswordResetRequest)

    suspend fun resetPassword(passwordReset: PasswordReset)

    suspend fun checkStaffOtpValidity(otp: String): Boolean
}
