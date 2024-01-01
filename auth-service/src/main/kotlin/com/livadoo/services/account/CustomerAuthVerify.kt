package com.livadoo.services.account

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CustomerAuthVerify(
    @NotBlank
    val phoneNumber: String,
    @NotBlank
    val regionCode: String,
    @Size(min = 6, max = 6)
    val verificationCode: String,
)
