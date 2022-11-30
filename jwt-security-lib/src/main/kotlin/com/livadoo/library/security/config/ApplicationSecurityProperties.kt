package com.livadoo.library.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "application.security")
data class ApplicationSecurityProperties(
    val jwt: Jwt
) {
    class Jwt(
        val secret: String,
    )
}