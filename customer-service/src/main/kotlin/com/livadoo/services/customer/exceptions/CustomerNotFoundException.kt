package com.livadoo.services.customer.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty


@ResponseErrorCode(ErrorCodes.CUSTOMER_NOT_FOUND)
class CustomerNotFoundException(
    @ResponseErrorProperty val customerId: String
) : ObjectNotFoundException("Could not find customer with id: $customerId")