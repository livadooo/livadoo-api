package com.livadoo.services.user.exceptions

import com.livadoo.services.common.exceptions.BadRequestException
import com.livadoo.services.common.exceptions.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_EMAIL_IN_USE)
class UserEmailTakenException(
    @ResponseErrorProperty val email: String
) : BadRequestException("Email address: $email is already in use")