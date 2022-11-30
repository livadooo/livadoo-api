package com.livadoo.proxy.notification.model

data class CustomerAccount(
    val userId: String,
    val email: String,
    val verificationKey: String
)
