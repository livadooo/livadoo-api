package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.BadRequestException
import com.livadoo.common.exceptions.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.AUTHORITY_INVALID)
class BadAuthorityException(
    @ResponseErrorProperty val authority: String
) : BadRequestException("Authority: $authority is invalid")