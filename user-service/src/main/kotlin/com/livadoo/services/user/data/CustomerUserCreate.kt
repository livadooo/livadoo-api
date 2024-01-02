package com.livadoo.services.user.data

import jakarta.validation.constraints.Size

data class CustomerUserCreate(
    @field:Size(max = 50, min = 2)
    val firstName: String,
    @field:Size(max = 50, min = 2)
    val lastName: String,
    val phoneNumber: String,
    val regionCode: String,
)
