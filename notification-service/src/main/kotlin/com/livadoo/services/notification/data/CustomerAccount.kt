package com.livadoo.services.notification.data

data class CustomerAccount(
    val userId: String,
    val email: String,
    val verificationKey: String
)
