package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ConflictingObjectException
import com.livadoo.common.exceptions.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_PHONE_IN_USE)
class UserPhoneTakenException(
    @ResponseErrorProperty val phoneNumber: String
) : ConflictingObjectException("Phone number: $phoneNumber is already in use")