package com.livadoo.services.user

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo.user.password-reset")
data class PasswordResetProperties(
    val validityTimeInHours: Int,
)
