package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ObjectAlreadyExistsException

class UserWithPhoneNumberAlreadyExistsException(
    phoneNumber: String
) : ObjectAlreadyExistsException("User already exists", "User with phone number: $phoneNumber already exists")