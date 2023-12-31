package com.livadoo.services.permission.exception

import com.livadoo.services.permission.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.PERMISSION_NOT_FOUND)
class PermissionNotFoundException(
    @ResponseErrorProperty val permissionId: String,
) : NotFoundException("Permission with id: $permissionId was not found")
