package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException

class UserNotFoundException(
    userId: String
) : ObjectNotFoundException("User not found", "User with id $userId not found")