package com.livadoo.services.customer.exceptions

import com.livadoo.services.common.exceptions.ErrorCodes
import com.livadoo.services.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ADDRESS_NOT_FOUND)
class AddressNotFoundException(
    @ResponseErrorProperty val addressId: String
) : ObjectNotFoundException("Could not find address with id: $addressId")