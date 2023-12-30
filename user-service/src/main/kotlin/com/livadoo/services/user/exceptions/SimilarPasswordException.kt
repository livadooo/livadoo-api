package com.livadoo.services.user.exceptions

import com.livadoo.services.common.exceptions.BadRequestException
import com.livadoo.services.common.exceptions.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.SIMILAR_PASSWORDS)
class SimilarPasswordException : BadRequestException("Passwords are too similar")