package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ConflictingObjectException
import com.livadoo.common.exceptions.ErrorCodes

class UserPhoneTakenException(
    phoneNumber: String
) : ConflictingObjectException(
    title = "Phone number in use",
    detail = "Phone number: $phoneNumber is already in use",
    statusCode = ErrorCodes.USER_PHONE_IN_USE
)