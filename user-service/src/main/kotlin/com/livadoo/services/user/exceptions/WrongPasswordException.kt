package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.BadRequestException
import com.livadoo.common.exceptions.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode

@ResponseErrorCode(ErrorCodes.WRONG_PASSWORD)
class WrongPasswordException(
) : BadRequestException("Wrong password")