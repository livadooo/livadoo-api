package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.UnauthorizedException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ACCOUNT_BLOCKED)
class AccountBlockedException(
    @ResponseErrorProperty val title: String = "Account blocked",
) : UnauthorizedException("Your account is blocked. Please, contact support to solve this issue.")