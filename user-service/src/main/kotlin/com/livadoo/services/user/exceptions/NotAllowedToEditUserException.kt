package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.ForbiddenException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.NOT_ALLOWED_TO_EDIT_USER)
class NotAllowedToEditUserException(
    detail: String = "You don't have sufficient permissions to edit this user",
) : ForbiddenException(detail)
