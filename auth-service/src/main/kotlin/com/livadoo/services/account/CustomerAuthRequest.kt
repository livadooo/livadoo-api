package com.livadoo.services.account

import jakarta.validation.constraints.NotBlank

data class CustomerAuthRequest(
    @NotBlank
    val phoneNumber: String,
    @NotBlank
    val regionCode: String,
)
