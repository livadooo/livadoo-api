package com.livadoo.services.user.exceptions

import com.livadoo.services.common.exceptions.ErrorCodes
import com.livadoo.services.common.exceptions.UnauthorizedException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ACCOUNT_DELETED)
class AccountDeletedException(
    @ResponseErrorProperty val title: String = "Account deleted",
) : UnauthorizedException("Your account is deleted")