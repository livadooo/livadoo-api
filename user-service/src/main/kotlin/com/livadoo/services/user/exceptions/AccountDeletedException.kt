package com.livadoo.services.user.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.UnauthorizedException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ACCOUNT_DELETED)
class AccountDeletedException(
    @ResponseErrorProperty val title: String = "Account deleted",
) : UnauthorizedException("Your account is deleted")
