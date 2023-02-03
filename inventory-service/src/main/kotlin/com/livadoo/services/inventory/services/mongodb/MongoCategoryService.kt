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

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.collections.HashSet

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
                    this.active = active
                    this.parentId = parentId
                    updatedAt = Instant.now()
                    updatedBy = currentUser.username
                }
                ?: throw CategoryNotFoundException(categoryId)

            categoryRepository.save(categoryEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw NotAllowedException()
        }
    }

    override suspend fun updateCategoryPicture(categoryId: String, filePart: FilePart): String {
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
        val category = categoryRepository.findById(categoryId).map { it.toDto() }.awaitSingleOrNull()
            ?: throw CategoryNotFoundException(categoryId)

        return category.apply {
            parentId?.let {
                val parent = categoryRepository.findById(parentId).awaitSingleOrNull()
                    ?: throw CategoryNotFoundException(parentId)
                parentName = parent.name
            }
        }
    }

    override suspend fun deleteCategory(categoryId: String) {
        categoryRepository.findById(categoryId).awaitSingleOrNull()
            ?.let { categoryRepository.deleteById(it.id!!).awaitSingleOrNull() }
            ?: throw CategoryNotFoundException(categoryId)
    }

    override suspend fun getCategories(pageable: Pageable, query: String): Page<Category> {
        val categoriesTuple = categoryRepository
            .findByNameLikeIgnoreCase(query, pageable)
            .map { it.toDto() }
            .collectList()
            .zipWith(categoryRepository.countByNameLikeIgnoreCase(query))
            .awaitFirst()
        val categories = categoriesTuple.t1
        val categoriesCount = categoriesTuple.t2

        val parentCategoryIds = HashSet<String>()

        categories.mapNotNull { it.parentId }.forEach { parentCategoryIds.add(it) }

        val parentCategories = if (parentCategoryIds.isNotEmpty()) {
            categoryRepository.findAllById(parentCategoryIds).collectList().awaitFirst()
        } else emptyList()

        if (parentCategories.isNotEmpty()) {
            categories.map { category ->
                category.apply {
                    parentName = parentId?.let {
                        parentCategories.find { parent -> parent.id == category.parentId }?.name
                            ?: throw CategoryNotFoundException(parentId)
                    }
                }
            }
        }

        return PageImpl(categories, pageable, categoriesCount)

    }

    private suspend fun uploadCategoryImage(filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()
        return storageService.uploadCategoryImage(filePart.filename(), contentType, contentBytes)
    }
}