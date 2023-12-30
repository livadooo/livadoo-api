package com.livadoo.services.user.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo.password-reset-key")
data class PasswordResetKeyProperties(val validityTimeInHours: Int)
