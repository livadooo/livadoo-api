package com.livadoo.services.user.services

import com.livadoo.services.user.security.LoginCredentials
import com.livadoo.services.user.data.PasswordReset
import com.livadoo.services.user.data.PasswordResetRequest
import com.livadoo.services.user.data.User
import com.livadoo.services.user.security.AuthUserDTO

interface AccountService {

    suspend fun login(credentials: LoginCredentials): AuthUserDTO

    suspend fun refreshToken(refreshToken: String): AuthUserDTO

    suspend fun loginAfterAccountActivation(email: String): AuthUserDTO

    suspend fun getCurrentUser(): User

    suspend fun requestPasswordReset(passwordResetRequest: PasswordResetRequest)

    suspend fun resetPassword(passwordReset: PasswordReset)
}