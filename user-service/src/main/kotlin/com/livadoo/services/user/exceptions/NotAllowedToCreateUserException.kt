package com.livadoo.services.user.exceptions

import com.livadoo.services.common.exceptions.ErrorCodes
import com.livadoo.services.common.exceptions.ForbiddenException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.NOT_ALLOWED_TO_CREATE_USER)
class NotAllowedToCreateUserException(
    detail: String = "You don't have sufficient permissions to create this user"
) : ForbiddenException(detail)