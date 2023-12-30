package com.livadoo.services.user.exceptions

import com.livadoo.services.common.exceptions.ErrorCodes
import com.livadoo.services.common.exceptions.ForbiddenException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.NOT_ALLOWED_TO_EDIT_USER)
class NotAllowedToEditUserException(
    detail: String = "You don't have sufficient permissions to edit this user"
) : ForbiddenException(detail)