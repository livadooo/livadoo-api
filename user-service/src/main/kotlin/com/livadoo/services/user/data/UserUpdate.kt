package com.livadoo.services.user.data

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserUpdate(
    @field:NotBlank
    val userId: String,
    @field:Size(max = 50, min = 2)
    val firstName: String,
    @field:Size(max = 50, min = 2)
    val lastName: String,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
)
