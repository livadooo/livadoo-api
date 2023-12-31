package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_NOT_FOUND)
class UserNotFoundException(
    @ResponseErrorProperty val userId: String,
) : NotFoundException("Could not find user with id: $userId")
