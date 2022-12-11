package com.livadoo.services.user.data

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class StaffUserCreate(
    @field:Size(message = "INVALID_FIRSTNAME", max = 50, min = 2)
    var firstName: String,
    @field:Size(message = "INVALID_LASTNAME", max = 50, min = 2)
    var lastName: String,
    var phoneNumber: String,
    @field:NotEmpty(message = "MISSING_AUTHORITY")
    var authority: String,
    @field:Email(
        message = "INVALID_EMAIL_ADDRESS", regexp =
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    )
    var email: String
)