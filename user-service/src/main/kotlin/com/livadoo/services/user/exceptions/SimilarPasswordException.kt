package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.BadRequestException
import com.livadoo.utils.exception.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.SIMILAR_PASSWORDS)
class SimilarPasswordException : BadRequestException("Passwords are too similar")
