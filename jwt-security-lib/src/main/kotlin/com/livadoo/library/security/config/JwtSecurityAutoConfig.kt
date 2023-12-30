package com.livadoo.library.security.config

import com.livadoo.library.security.jwt.JwtValidator
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@EnableConfigurationProperties(SecurityProperties::class)
class JwtSecurityAutoConfig(
    private val securityProperties: SecurityProperties
) {

    init {
        if (securityProperties.secret.isBlank()) {
            throw IllegalArgumentException("Invalid secret key for JWT")
        }
    }

    @Bean
    fun jwtValidator(): JwtValidator = JwtValidator(securityProperties.secret)
}