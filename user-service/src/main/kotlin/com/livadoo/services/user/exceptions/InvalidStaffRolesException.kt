package com.livadoo.services.user.exceptions

import com.livadoo.services.user.ErrorCodes
import com.livadoo.utils.exception.BadRequestException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.INVALID_STAFF_ROLES)
class InvalidStaffRolesException : BadRequestException("The roles provided were incorrect")
