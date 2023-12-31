package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ConflictException
import com.livadoo.utils.exception.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_EMAIL_IN_USE)
class UserEmailTakenException(
    @ResponseErrorProperty val email: String,
) : ConflictException("Email address: $email is already in use")
