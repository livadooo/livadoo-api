package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.UnauthorizedException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ACCOUNT_NOT_VERIFIED)
class AccountNotVerifiedException(
    @ResponseErrorProperty val title: String = "Account not verified",
) : UnauthorizedException("Your account is not yet verified")
