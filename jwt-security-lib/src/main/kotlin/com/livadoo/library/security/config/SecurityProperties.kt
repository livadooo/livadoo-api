package com.livadoo.library.security.config

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