package com.livadoo.services.customer.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException


class CustomerNotFoundException(
    customerId: String
) : ObjectNotFoundException(
    title = "Customer not found",
    detail = "Could not find customer with id: $customerId",
    statusCode = ErrorCodes.CUSTOMER_NOT_FOUND
)