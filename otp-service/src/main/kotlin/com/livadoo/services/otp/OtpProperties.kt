package com.livadoo.services.otp

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo.otp")
data class OtpProperties(
    val customerAuth: CustomerAuth,
    val staffPasswordReset: StaffPasswordReset,
) {
    data class CustomerAuth(val validityTimeInMinutes: Int)
    data class StaffPasswordReset(val validityTimeInHours: Int, val adminxUrl: String)
}
