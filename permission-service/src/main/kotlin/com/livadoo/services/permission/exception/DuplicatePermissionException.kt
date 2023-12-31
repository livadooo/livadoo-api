package com.livadoo.services.permission.exception

import com.livadoo.services.permission.ErrorCodes
import com.livadoo.utils.exception.ConflictException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.PERMISSION_IN_USE)
class DuplicatePermissionException(
    @ResponseErrorProperty val permission: String,
) : ConflictException("Permission: $permission is already in use")
