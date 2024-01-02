package com.livadoo.services.user.exceptions

import com.livadoo.services.user.ErrorCodes
import com.livadoo.utils.exception.BadRequestException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.INVALID_PHONE_NUMBER)
class InvalidPhoneNumberException(
    @ResponseErrorProperty val phoneNumber: String,
) : BadRequestException("Phone number $phoneNumber is invalid")
