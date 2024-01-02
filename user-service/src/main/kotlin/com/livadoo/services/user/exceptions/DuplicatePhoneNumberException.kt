package com.livadoo.services.user.exceptions

import com.livadoo.services.user.ErrorCodes
import com.livadoo.utils.exception.ConflictException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_PHONE_NUMBER_IN_USE)
class DuplicatePhoneNumberException(
    @ResponseErrorProperty val phoneNumber: String,
) : ConflictException("User with phone number: $phoneNumber is already in use")
