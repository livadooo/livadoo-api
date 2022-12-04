package com.livadoo.services.user.data

import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class CustomerUserCreate(
    @field:Size(message = "INVALID_FIRSTNAME", max = 50, min = 2)
    val firstName: String,
    @field:Size(message = "INVALID_LASTNAME", max = 50, min = 2)
    val lastName: String,
    val phoneNumber: String,
    @field:Email(
        message = "INVALID_EMAIL_ADDRESS", regexp =
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    )
    val email: String,
    val password: String
)