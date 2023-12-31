package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.AUTHORITY_NOT_FOUND)
class AuthorityNotFoundException(
    @ResponseErrorProperty val authority: String,
) : NotFoundException("Could not find authority: $authority")
