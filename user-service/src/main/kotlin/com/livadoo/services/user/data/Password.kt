package com.livadoo.services.user.data

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class PasswordResetRequest(
    @field:Email(
        message = "INVALID_EMAIL_ADDRESS", regexp =
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    )
    val email: String
)

data class PasswordReset(val resetKey: String, val newPassword: String)

data class PasswordUpdate(
    @field:Size(message = "Le mot de passe doit avoir au moins 8 caractères", min = 8)
    var oldPassword: String,
    @field:Size(message = "Le mot de passe doit avoir au moins 8 caractères", min = 8)
    var newPassword: String
)
