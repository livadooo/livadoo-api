package com.livadoo.services.role

import jakarta.validation.constraints.NotBlank

data class RoleCreate(
    @field:NotBlank val role: String,
    @field:NotBlank val title: String,
    @field:NotBlank val description: String,
)
