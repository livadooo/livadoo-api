package com.livadoo.library.security.config

import com.livadoo.library.security.jwt.JwtValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ApplicationSecurityProperties::class)
class JwtSecurityAutoConfiguration @Autowired constructor(
    private val securityProperties: ApplicationSecurityProperties
) {

    init {
        if (securityProperties.jwt.secret.isBlank()) {
            throw IllegalArgumentException("Invalid secret key for JWT")
        }
    }

    @Bean
    fun jwtValidator(): JwtValidator = JwtValidator(securityProperties)
}