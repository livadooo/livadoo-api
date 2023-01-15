package com.livadoo.services.inventory.exceptions

import com.livadoo.services.common.exceptions.ErrorCodes
import com.livadoo.services.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.PRODUCT_NOT_FOUND)
class ProductNotFoundException(
    @ResponseErrorProperty val productId: String
) : ObjectNotFoundException("Could not find product with id: $productId")