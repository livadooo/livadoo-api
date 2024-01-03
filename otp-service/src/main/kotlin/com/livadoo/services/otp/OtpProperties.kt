package com.livadoo.services.otp

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo.otp")
data class OtpProperties(
    val customerAuth: CustomerAuth,
    val passwordReset: PasswordReset,
) {
    data class CustomerAuth(val validityTimeInMinutes: Int)
    data class PasswordReset(val validityTimeInHours: Int)
}
