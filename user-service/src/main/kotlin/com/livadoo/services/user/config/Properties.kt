package com.livadoo.services.user.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "application.security")
data class SecurityProperties(
    val jwt: Jwt
) {
    class Jwt(
        val secret: String,
        val tokenValidityInSeconds: Long,
        val refreshSecret: String,
        val refreshTokenValidityInSeconds: Long
    )
}

@ConfigurationProperties(prefix = "application.password-reset-key")
data class PasswordResetKeyProperties(val validityTimeInHours: Int)
