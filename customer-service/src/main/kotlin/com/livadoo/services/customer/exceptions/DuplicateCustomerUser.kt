package com.livadoo.services.customer.exceptions

import com.livadoo.services.customer.ErrorCodes
import com.livadoo.utils.exception.ConflictException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.CUSTOM_USER_IN_USE)
class DuplicateCustomerUser(
    @ResponseErrorProperty val userId: String,
) : ConflictException("Customer with user id: $userId is already in use")
