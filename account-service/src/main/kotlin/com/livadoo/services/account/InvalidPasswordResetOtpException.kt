package com.livadoo.services.account

import com.livadoo.utils.exception.BadRequestException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.INVALID_PASSWORD_RESET_OTP)
class InvalidPasswordResetOtpException(
    @ResponseErrorProperty val otp: String,
) : BadRequestException("Invalid password reset otp")
