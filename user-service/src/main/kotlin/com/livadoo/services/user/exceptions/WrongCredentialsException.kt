package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.UnauthorizedException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.WRONG_CREDENTIALS)
class WrongCredentialsException(
    @ResponseErrorProperty val title: String = "Wrong credentials",
) : UnauthorizedException("Incorrect email or password")
