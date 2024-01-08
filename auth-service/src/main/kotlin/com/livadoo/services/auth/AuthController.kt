package com.livadoo.services.auth

import com.livadoo.utils.user.AuthUserDto
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/customers/request")
    suspend fun requestCustomerAuth(
        @Valid @RequestBody
        authRequest: CustomerAuthRequest,
    ) {
        authService.requestCustomerAuth(authRequest)
    }

    @PostMapping("/customers/verify")
    suspend fun verifyCustomerAuth(
        @Valid @RequestBody
        authVerify: CustomerAuthVerify,
    ): AuthUserDto {
        return authService.verifyCustomerAuth(authVerify)
    }

    @PostMapping("/staff")
    suspend fun authenticateStaff(
        @Valid @RequestBody
        staffAuthCredentials: StaffAuthCredentials,
    ): AuthUserDto {
        return authService.authenticateStaff(staffAuthCredentials)
    }

    @PostMapping("/token/refresh")
    suspend fun refreshToken(
        @Valid @RequestBody
        refreshToken: RefreshToken,
    ): AuthUserDto {
        return authService.refreshToken(refreshToken)
    }
}
