package com.livadoo.services.user.exceptions

import com.livadoo.services.user.ErrorCodes
import com.livadoo.utils.exception.ConflictException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_EMAIL_IN_USE)
class DuplicateEmailException(
    @ResponseErrorProperty val email: String,
) : ConflictException("User with email: $email is already in use")
