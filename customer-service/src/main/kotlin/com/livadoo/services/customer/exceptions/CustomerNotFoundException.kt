package com.livadoo.services.customer.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.CUSTOMER_NOT_FOUND)
class CustomerNotFoundException(
    @ResponseErrorProperty val customerId: String,
) : NotFoundException("Could not find customer with id: $customerId")
