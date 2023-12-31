package com.livadoo.utils.security.config

import com.livadoo.utils.security.jwt.JwtValidator
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@EnableConfigurationProperties(SecurityProperties::class)
class JwtSecurityAutoConfig(
    private val securityProperties: SecurityProperties,
) {
    init {
        if (securityProperties.secret.isBlank()) {
            throw IllegalArgumentException("Invalid secret key for JWT")
        }
    }

    @Bean
    fun jwtValidator(): JwtValidator = JwtValidator(securityProperties.secret)
}
