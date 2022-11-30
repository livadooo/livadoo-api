package com.livadoo.services.customer.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException

class AddressNotFoundException(
    addressId: String
) : ObjectNotFoundException("Address not found", "Address with id $addressId not found")