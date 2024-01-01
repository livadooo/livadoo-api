package com.livadoo.services.account

import com.livadoo.utils.user.AuthUserDto
import org.springframework.stereotype.Service

@Service
class DefaultAuthService(
    private val authRepository: AuthRepository,
) : AuthService {
    override suspend fun requestCustomerAuth(authRequest: CustomerAuthRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun verifyCustomerAuth(authVerify: CustomerAuthVerify): AuthUserDto {
        TODO("Not yet implemented")
    }

    override suspend fun authenticateStaff(authCredentials: StaffAuthCredentials): AuthUserDto {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(refreshToken: RefreshToken): AuthUserDto {
        TODO("Not yet implemented")
    }
}
