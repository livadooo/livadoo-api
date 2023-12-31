package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ConflictException
import com.livadoo.utils.exception.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_PHONE_IN_USE)
class UserPhoneTakenException(
    @ResponseErrorProperty val phoneNumber: String,
) : ConflictException("Phone number: $phoneNumber is already in use")
