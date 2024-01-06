package com.livadoo.services.auth

import jakarta.validation.constraints.Size

data class CustomerAuthVerify(
    @Size(min = 6, max = 6)
    val otp: String,
)
