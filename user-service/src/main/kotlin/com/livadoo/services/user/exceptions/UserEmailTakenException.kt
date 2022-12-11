package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ConflictingObjectException
import com.livadoo.common.exceptions.ErrorCodes

class UserEmailTakenException(
    email: String
) : ConflictingObjectException(
    title = "Email address in use",
    detail = "Email address: $email is already in use",
    statusCode = ErrorCodes.USER_EMAIL_IN_USE
)