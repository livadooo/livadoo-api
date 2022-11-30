package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ObjectAlreadyExistsException

class UserWithEmailAlreadyExistsException(
    email: String
) : ObjectAlreadyExistsException("User already exists", "User with email: $email already exists")