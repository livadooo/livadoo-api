package com.livadoo.services.user.data

import java.time.Instant
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class User(
    @field:Size(message = "INVALID_FIRSTNAME", max = 50, min = 2)
    var firstName: String,
    @field:Size(message = "INVALID_LASTNAME", max = 50, min = 2)
    var lastName: String,
    var phoneNumber: String,
    @field:NotEmpty(message = "MISSING_AUTHORITY")
    var authorities: List<String>,
    @field:Email(
        message = "INVALID_EMAIL_ADDRESS", regexp =
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    )
    var email: String,
    var avatarId: String? = null,
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
    var userId: String? = null
)
