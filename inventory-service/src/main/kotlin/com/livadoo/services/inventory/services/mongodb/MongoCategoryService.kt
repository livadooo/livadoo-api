package com.livadoo.services.inventory.services.mongodb

import com.livadoo.services.common.exceptions.NotAllowedException
import com.livadoo.services.common.exceptions.NotAuthenticatedException
import com.livadoo.services.common.utils.extractContent
import com.livadoo.library.security.utils.currentAuthUser
import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.inventory.data.Category
import com.livadoo.services.inventory.data.CategoryCreate
import com.livadoo.services.inventory.data.CategoryEdit
import com.livadoo.services.inventory.exceptions.CategoryNotFoundException
import com.livadoo.services.inventory.services.CategoryService
import com.livadoo.services.inventory.services.mongodb.entity.CategoryEntity
import com.livadoo.services.inventory.services.mongodb.entity.toDto
import com.livadoo.services.inventory.services.mongodb.repository.CategoryRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class MongoCategoryService @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val storageService: StorageServiceProxy,
) : CategoryService {

    override suspend fun createCategory(categoryCreate: CategoryCreate, filePart: FilePart): Category {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw NotAuthenticatedException()

        return if (currentUser.isStaff) {
            val pictureUrl = uploadCategoryImage(filePart)
            val (name, description, parentId) = categoryCreate
            val categoryEntity = CategoryEntity(name, description, parentId, pictureUrl, true, createdBy = currentUser.username)

            categoryRepository.save(categoryEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw NotAllowedException()
        }
    }

    override suspend fun updateCategory(categoryEdit: CategoryEdit): Category {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw NotAuthenticatedException()

        return if (currentUser.isStaff) {
            val (name, description, parentId, active, categoryId) = categoryEdit
            parentId?.let { categoryRepository.findById(it).awaitSingleOrNull() ?: throw CategoryNotFoundException(it) }

            val categoryEntity = categoryRepository.findById(categoryId).awaitSingleOrNull()
                ?.apply {
                    this.name = name
                    this.description = description
                    this.parentId = categoryId
                    this.active = active
                    this.parentId = parentId
                }
                ?: throw CategoryNotFoundException(categoryId)

            categoryRepository.save(categoryEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw NotAllowedException()
        }
    }

    override suspend fun updateCategoryImage(categoryId: String, filePart: FilePart): String {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw NotAuthenticatedException()

        return if (currentUser.isStaff) {
            val categoryEntity = categoryRepository.findById(categoryId).awaitSingleOrNull()
                ?: throw CategoryNotFoundException(categoryId)

            categoryEntity.pictureUrl = uploadCategoryImage(filePart)

            categoryRepository.save(categoryEntity).map { it.toDto() }.awaitSingle().pictureUrl
        } else {
            throw NotAllowedException()
        }
    }

    override suspend fun getCategory(categoryId: String): Category {
        return categoryRepository.findById(categoryId).map { it.toDto() }.awaitSingleOrNull()
            ?: throw CategoryNotFoundException(categoryId)
    }

    override suspend fun deleteCategory(categoryId: String) {
        categoryRepository.findById(categoryId).awaitSingleOrNull()
            ?.let { categoryRepository.deleteById(it.id!!).awaitSingleOrNull() }
            ?: throw CategoryNotFoundException(categoryId)
    }

    override suspend fun getCategories(categoryParentId: String?, active: Boolean, pageable: Pageable): Pair<List<Category>, Long> {
        val categories = categoryRepository
            .findAllByParentIdAndActive(categoryParentId, active, pageable)
            .map { it.toDto() }
            .asFlow()
            .toList()

        val categoriesCount = categoryRepository.countAllByParentIdAndActive(categoryParentId, active).awaitSingle()

        return categories to categoriesCount
    }

    private suspend fun uploadCategoryImage(filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()
        return storageService.uploadCategoryImage(filePart.filename(), contentType, contentBytes)
    }
}