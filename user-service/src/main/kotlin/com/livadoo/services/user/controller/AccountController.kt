package com.livadoo.services.user.controller

import com.livadoo.services.user.data.PasswordReset
import com.livadoo.services.user.data.PasswordResetRequest
import com.livadoo.services.user.data.User
import com.livadoo.services.user.security.AuthUserDTO
import com.livadoo.services.user.security.LoginCredentials
import com.livadoo.services.user.services.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/v1/users")
class AccountController @Autowired constructor(
    private val accountService: AccountService
) {

    @PostMapping("/login")
    suspend fun authenticate(@Valid @RequestBody credentials: LoginCredentials): ResponseEntity<AuthUserDTO> {
        val authUserDto = accountService.login(credentials)
        return ResponseEntity.ok(authUserDto)
    }

    @PostMapping("/token/refresh")
    suspend fun refreshToken(@Valid @RequestBody token: RefreshToken): ResponseEntity<AuthUserDTO> {
        val authUserDto = accountService.refreshToken(token.refreshToken)
        return ResponseEntity.ok(authUserDto)
    }

    @GetMapping("/account")
    suspend fun getCurrentUserInfo(): User {
        return accountService.getCurrentUser()
    }

    @PostMapping("/reset-password")
    suspend fun resetPassword(@RequestBody passwordReset: PasswordReset) {
        return accountService.resetPassword(passwordReset)
    }

    @PostMapping("/request-password-reset")
    suspend fun requestPasswordReset(@RequestBody passwordResetRequest: PasswordResetRequest) {
        return accountService.requestPasswordReset(passwordResetRequest)
    }

    class RefreshToken(val refreshToken: String)
}