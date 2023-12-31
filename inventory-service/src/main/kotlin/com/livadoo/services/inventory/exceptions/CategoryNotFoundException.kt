package com.livadoo.services.inventory.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.CATEGORY_NOT_FOUND)
class CategoryNotFoundException(
    @ResponseErrorProperty val categoryId: String,
) : NotFoundException("Could not find category with address with id: $categoryId")
