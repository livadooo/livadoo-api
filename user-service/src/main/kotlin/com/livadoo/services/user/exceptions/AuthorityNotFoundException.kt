package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.AUTHORITY_NOT_FOUND)
class AuthorityNotFoundException(
    @ResponseErrorProperty val authority: String
) : ObjectNotFoundException("Could not find authority: $authority")