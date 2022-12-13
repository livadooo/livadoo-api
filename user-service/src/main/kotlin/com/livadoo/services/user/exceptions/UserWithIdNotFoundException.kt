package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.USER_WITH_ID_NOT_FOUND)
class UserWithIdNotFoundException(
    @ResponseErrorProperty val userId: String
) : ObjectNotFoundException("Could not find user with id: $userId")