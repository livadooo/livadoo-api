package com.livadoo.services.common.exceptions

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.FORBIDDEN)
class NotAllowedException(
    detail: String = "You don't have sufficient permissions to perform this operation"
) : ForbiddenException(detail)