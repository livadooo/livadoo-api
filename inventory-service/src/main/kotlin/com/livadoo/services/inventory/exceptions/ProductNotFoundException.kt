package com.livadoo.services.inventory.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException

class ProductNotFoundException(
    productId: String
) : ObjectNotFoundException("Product not found", "Product with id $productId not found")