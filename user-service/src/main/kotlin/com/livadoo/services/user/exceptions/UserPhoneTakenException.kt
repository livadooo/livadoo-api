package com.livadoo.services.user.exceptions

import com.livadoo.services.common.exceptions.BadRequestException
import com.livadoo.services.common.exceptions.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_PHONE_IN_USE)
class UserPhoneTakenException(
    @ResponseErrorProperty val phoneNumber: String
) : BadRequestException("Phone number: $phoneNumber is already in use")