package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException

class UserWithIdNotFoundException(
    userId: String
) : ObjectNotFoundException(
    title = "User not found",
    detail = "Could not find user with id: $userId",
    statusCode = ErrorCodes.USER_WITH_ID_NOT_FOUND
)