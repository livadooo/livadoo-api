package com.livadoo.services.phone.validation

import com.livadoo.utils.exception.BadRequestException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.INVALID_PHONE_NUMBER)
class InvalidPhoneNumberException(
    @ResponseErrorProperty val phoneNumber: String,
) : BadRequestException("Phone number $phoneNumber is invalid")
