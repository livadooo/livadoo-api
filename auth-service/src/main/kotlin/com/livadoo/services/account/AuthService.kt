package com.livadoo.services.account

import com.livadoo.utils.user.AuthUserDto

interface AuthService {
    suspend fun requestCustomerAuth(authRequest: CustomerAuthRequest)

    suspend fun verifyCustomerAuth(authVerify: CustomerAuthVerify): AuthUserDto

    suspend fun authenticateStaff(authCredentials: StaffAuthCredentials): AuthUserDto

    suspend fun refreshToken(refreshToken: RefreshToken): AuthUserDto
}
