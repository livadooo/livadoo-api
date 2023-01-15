package com.livadoo.services.inventory.services

import com.livadoo.services.inventory.data.Category
import com.livadoo.services.inventory.data.CategoryCreate
import com.livadoo.services.inventory.data.CategoryEdit
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart

interface CategoryService {

    suspend fun createCategory(categoryCreate: CategoryCreate, filePart: FilePart): Category

    suspend fun updateCategory(categoryEdit: CategoryEdit): Category

    suspend fun updateCategoryImage(categoryId: String, filePart: FilePart): String

    suspend fun getCategory(categoryId: String): Category

    suspend fun deleteCategory(categoryId: String)

    suspend fun getCategories(categoryParentId: String?, active: Boolean, pageable: Pageable): Pair<List<Category>, Long>
}