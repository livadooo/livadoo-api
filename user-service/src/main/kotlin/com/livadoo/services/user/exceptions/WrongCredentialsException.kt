package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.UnauthorizedException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.WRONG_CREDENTIALS)
class WrongCredentialsException(
    @ResponseErrorProperty val title: String = "Wrong credentials",
) : UnauthorizedException("Incorrect email or password")