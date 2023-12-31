package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.SECURE_KEY_NOT_FOUND)
class SecureKeyNotFoundException(
    @ResponseErrorProperty val secureKey: String,
) : NotFoundException("Could not find secure key: $secureKey")
