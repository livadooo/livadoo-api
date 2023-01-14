package com.livadoo.services.customer.data

import com.livadoo.proxy.user.model.User

data class CustomerUser(
    var customerId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val city: String?,
    val country: String?,
    val address: String?,
    var authority: String,
    val email: String,
    val userId: String,
    val avatarUrl: String? = null
) {
    constructor(customer: Customer, user: User) : this(
        customer.customerId,
        user.firstName,
        user.lastName,
        user.phoneNumber,
        user.city,
        user.country,
        user.address,
        user.authority,
        user.email,
        user.userId,
        user.avatarUrl,
    )
}