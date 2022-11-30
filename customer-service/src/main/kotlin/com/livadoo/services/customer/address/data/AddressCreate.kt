package com.livadoo.services.customer.address.data

data class AddressCreate(
    val countryCode: String,
    val fullName: String,
    val phoneNumber: String,
    val address: String,
    val city: String,
    val region: String,
    val isDefault: Boolean
)