package com.livadoo.services.customer.exceptions

import com.livadoo.utils.exception.ConflictException
import com.livadoo.utils.exception.ErrorCodes
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.ADDRESS_NOT_FOUND)
class AddressNotFoundException(
    @ResponseErrorProperty val addressId: String,
) : ConflictException("Could not find address with id: $addressId")
