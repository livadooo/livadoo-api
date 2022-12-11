package com.livadoo.services.inventory.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException


class CategoryNotFoundException(
    categoryId: String
) : ObjectNotFoundException(
    title = "Category not found",
    detail = "Could not find category with id: $categoryId",
    statusCode = ErrorCodes.CATEGORY_NOT_FOUND
)