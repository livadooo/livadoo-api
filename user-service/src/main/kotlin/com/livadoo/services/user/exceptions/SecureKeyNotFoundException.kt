package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.SECURE_KEY_NOT_FOUND)
class SecureKeyNotFoundException(
     @ResponseErrorProperty val secureKey: String
) : ObjectNotFoundException("Could not find secure key: $secureKey")