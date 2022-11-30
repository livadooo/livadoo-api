package com.livadoo.services.inventory.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException

class CategoryNotFoundException(
    categoryId: String
) : ObjectNotFoundException("Category not found", "Category with id $categoryId not found")