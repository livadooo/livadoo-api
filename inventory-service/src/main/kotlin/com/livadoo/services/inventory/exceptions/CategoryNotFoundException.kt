package com.livadoo.services.inventory.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.CATEGORY_NOT_FOUND)
class CategoryNotFoundException(
    @ResponseErrorProperty val categoryId: String
) : ObjectNotFoundException("Could not find category with address with id: $categoryId")