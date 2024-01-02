package com.livadoo.services.auth

import com.livadoo.utils.user.AuthUserDto

interface AuthService {
    suspend fun requestCustomerAuth(authRequest: CustomerAuthRequest)

    suspend fun verifyCustomerAuth(authVerify: CustomerAuthVerify): AuthUserDto

    suspend fun authenticateStaff(credentials: StaffAuthCredentials): AuthUserDto

    suspend fun refreshToken(refreshToken: RefreshToken): AuthUserDto
}
