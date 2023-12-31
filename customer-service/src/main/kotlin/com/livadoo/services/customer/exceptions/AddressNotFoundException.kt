package com.livadoo.services.customer.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ADDRESS_NOT_FOUND)
class AddressNotFoundException(
    @ResponseErrorProperty val addressId: String,
) : NotFoundException("Could not find address with id: $addressId")
