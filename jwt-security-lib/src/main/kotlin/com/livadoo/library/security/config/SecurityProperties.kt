package com.livadoo.library.security.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo.jwt.security")
data class SecurityProperties(
    val secret: String,
    val refreshSecret: String,
    val tokenValidityInSeconds: Long,
    val refreshTokenValidityInSeconds: Long,
    val staffTokenValidityInSeconds: Long,
    val staffRefreshTokenValidityInSeconds: Long
) {
}
