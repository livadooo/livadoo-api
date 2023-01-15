package com.livadoo.services.user.exceptions

import com.livadoo.services.common.exceptions.BadRequestException
import com.livadoo.services.common.exceptions.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.SECURE_KEY_EXPIRED)
class SecureKeyExpiredException(
    @ResponseErrorProperty val secureKey: String
) : BadRequestException("Secure key: $secureKey expired")