package com.livadoo.services.inventory.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException

class ProductNotFoundException(
    productId: String
) : ObjectNotFoundException(
    title = "Product not found",
    detail = "Could not find product with id: $productId",
    statusCode = ErrorCodes.PRODUCT_NOT_FOUND
)