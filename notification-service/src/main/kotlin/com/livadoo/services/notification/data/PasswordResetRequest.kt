package com.livadoo.services.notification.data

data class PasswordResetRequest(
    val userId: String,
    val email: String,
    val resetKey: String,
    val expirationTime: Int
)