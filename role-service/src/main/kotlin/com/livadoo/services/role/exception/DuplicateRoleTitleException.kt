package com.livadoo.services.role.exception

import com.livadoo.services.role.ErrorCodes
import com.livadoo.utils.exception.ConflictException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ROLE_TITLE_IN_USE)
class DuplicateRoleTitleException(
    @ResponseErrorProperty val title: String,
) : ConflictException("Role with title: $title is already in use")
