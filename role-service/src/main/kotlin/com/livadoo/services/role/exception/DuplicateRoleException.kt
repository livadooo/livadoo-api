package com.livadoo.services.role.exception

import com.livadoo.services.role.ErrorCodes
import com.livadoo.utils.exception.ConflictException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ROLE_IN_USE)
class DuplicateRoleException(
    @ResponseErrorProperty val role: String,
) : ConflictException("Role: $role is already in use")
