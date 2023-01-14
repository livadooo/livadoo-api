package com.livadoo.proxy.user.model

data class User(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val city: String?,
    val country: String?,
    val address: String?,
    var authority: String,
    val avatarUrl: String?,
    val userId: String,
)
