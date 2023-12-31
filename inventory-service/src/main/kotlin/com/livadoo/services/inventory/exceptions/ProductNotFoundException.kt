package com.livadoo.services.inventory.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.PRODUCT_NOT_FOUND)
class ProductNotFoundException(
    @ResponseErrorProperty val productId: String,
) : NotFoundException("Could not find product with id: $productId")
