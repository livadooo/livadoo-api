package com.livadoo.common.exceptions

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.NOT_AUTHENTICATED)
class NotAuthenticatedException(
    @ResponseErrorProperty val title: String = "Not authenticated",
) : UnauthorizedException("You are not authenticated to access this resource")