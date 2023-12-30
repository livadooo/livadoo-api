package com.livadoo.services.user.data

import jakarta.validation.constraints.Size
import java.time.Instant

data class UserUpdate(
    @field:Size(message = "INVALID_FIRSTNAME", max = 50, min = 2)
    val firstName: String,
    @field:Size(message = "INVALID_LASTNAME", max = 50, min = 2)
    val lastName: String,
    val phoneNumber: String,
    val userId: String,
    val authority: String? = null,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val verified: Boolean = false,
    val blocked: Boolean = false,
    val deleted: Boolean = false,
    val createdBy: String? = null,
    val createdAt: Instant? = null,
    val updatedBy: String? = null,
    val updatedAt: Instant? = null,
)