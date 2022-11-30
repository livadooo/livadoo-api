package com.livadoo.services.inventory.services

import com.livadoo.services.inventory.data.Category
import com.livadoo.services.inventory.data.CategoryCreate
import com.livadoo.services.inventory.data.CategoryEdit
import com.livadoo.services.inventory.data.Product
import com.livadoo.services.inventory.data.ProductCreate
import com.livadoo.services.inventory.data.ProductEdit
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart

interface CategoryService {

    suspend fun createCategory(categoryCreate: CategoryCreate, filePart: FilePart): Category

    suspend fun updateCategory(categoryEdit: CategoryEdit): Category

    suspend fun updateCategoryPicture(categoryId: String, filePart: FilePart): Category

    suspend fun getCategory(categoryId: String): Category

    suspend fun deleteCategory(categoryId: String)

    suspend fun getCategories(categoryParentId: String?, active: Boolean, pageable: Pageable): Pair<List<Category>, Long>
}