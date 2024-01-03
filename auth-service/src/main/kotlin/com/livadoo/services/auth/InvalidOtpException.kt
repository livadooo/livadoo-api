package com.livadoo.services.auth

import com.livadoo.utils.exception.UnauthorizedException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.INVALID_OTP)
class InvalidOtpException(
    @ResponseErrorProperty val otp: String,
) : UnauthorizedException("The one time password: $otp is invalid")
