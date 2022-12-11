package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException

class InvalidSecureKeyException(
    secureKey: String
) : ObjectNotFoundException(
    title = "Incorrect or expired secure key",
    detail = "Secure key: $secureKey was either incorrect or expired",
    statusCode = ErrorCodes.INVALID_SECURE_KEY
)