package com.livadoo.services.role.exception

import com.livadoo.services.role.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ROLE_NOT_FOUND)
class RoleNotFoundException(
    @ResponseErrorProperty val roleId: String,
) : NotFoundException("Role with id: $roleId was not found")
