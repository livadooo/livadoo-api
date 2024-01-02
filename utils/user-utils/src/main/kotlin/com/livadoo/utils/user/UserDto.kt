package com.livadoo.utils.user

import jakarta.validation.constraints.Email
import java.time.Instant

data class UserDto(
    var firstName: String,
    var lastName: String,
    var phoneNumber: String?,
    var roles: List<String>,
    var permissions: List<String>,
    @field:Email(
        regexp =
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@" +
            "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
            "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
            "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
            "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|" +
            "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$",
    )
    var email: String?,
    var photoUrl: String? = null,
    var address: String? = null,
    var city: String? = null,
    var country: String? = null,
    var verified: Boolean = false,
    var blocked: Boolean = false,
    var deleted: Boolean = false,
    var createdBy: String? = null,
    var createdAt: Instant? = null,
    var updatedBy: String? = null,
    var updatedAt: Instant? = null,
    var userId: String,
    var language: Language,
    var activated: Boolean,
)
