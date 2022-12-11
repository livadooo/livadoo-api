package com.livadoo.services.customer.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException

class AddressNotFoundException(
    addressId: String
) : ObjectNotFoundException(
    title = "Address not found",
    detail = "Could not find address with id: $addressId",
    statusCode = "40401"
)