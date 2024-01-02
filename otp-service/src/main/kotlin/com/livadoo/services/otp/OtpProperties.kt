package com.livadoo.services.otp

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo.otp")
data class OtpProperties(
    val customerAuth: CustomerAuth,
) {
    data class CustomerAuth(val expirationTimeInMinutes: Long)
}
