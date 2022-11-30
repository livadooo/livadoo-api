package com.livadoo.proxy.notification.model

data class PasswordResetRequest(
    val userId: String,
    val email: String,
    val resetKey: String,
    val expirationTime: Int
)