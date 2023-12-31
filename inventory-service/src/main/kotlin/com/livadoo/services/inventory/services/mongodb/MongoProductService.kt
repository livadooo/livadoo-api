package com.livadoo.services.inventory.services.mongodb

import com.livadoo.library.security.utils.currentAuthUser
import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.inventory.data.Product
import com.livadoo.services.inventory.data.ProductCreate
import com.livadoo.services.inventory.data.ProductEdit
import com.livadoo.services.inventory.exceptions.CategoryNotFoundException
import com.livadoo.services.inventory.exceptions.ProductNotFoundException
import com.livadoo.services.inventory.services.ProductService
import com.livadoo.services.inventory.services.mongodb.entity.ProductEntity
import com.livadoo.services.inventory.services.mongodb.entity.toDto
import com.livadoo.services.inventory.services.mongodb.repository.CategoryRepository
import com.livadoo.services.inventory.services.mongodb.repository.ProductRepository
import com.livadoo.utils.exception.ForbiddenException
import com.livadoo.utils.exception.UnauthorizedException
import com.livadoo.utils.spring.extractContent
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class MongoProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val storageService: StorageServiceProxy,
) : ProductService {
    override suspend fun createProduct(
        productCreate: ProductCreate,
        filePart: FilePart,
    ): Product {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw UnauthorizedException("You are not authenticated")

        return if (currentUser.isAdmin) {
            val coverPictureUrl = uploadProductImage(filePart)
            val (name, description, categoryId, quantity, price) = productCreate
            val productEntity =
                ProductEntity(name, description, categoryId, quantity, price, true, coverPictureUrl, createdBy = currentUser.username)

            productRepository.save(productEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw ForbiddenException("Access denied")
        }
    }

    override suspend fun updateProduct(productEdit: ProductEdit): Product {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw UnauthorizedException("You are not authenticated")

        return if (currentUser.isAdmin) {
            val (name, description, categoryId, quantity, price, active, productId, discountPrice) = productEdit

            val productEntity =
                productRepository.findById(productId).awaitSingleOrNull()
                    ?.apply {
                        this.name = name
                        this.description = description
                        this.categoryId = categoryId
                        this.quantity = quantity
                        this.price = price
                        this.active = active
                        this.discountPrice = discountPrice
                    }
                    ?: throw ProductNotFoundException(productId)

            productRepository.save(productEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw ForbiddenException("Access denied")
        }
    }

    override suspend fun updateProductPicture(
        productId: String,
        filePart: FilePart,
    ): String {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw UnauthorizedException("You are not authenticated")

        return if (currentUser.isAdmin) {
            val productEntity =
                productRepository.findById(productId).awaitSingleOrNull()
                    ?: throw ProductNotFoundException(productId)

            productEntity.pictureUrl = uploadProductImage(filePart)

            productRepository.save(productEntity).map { it.toDto() }.awaitSingle().pictureUrl
        } else {
            throw ForbiddenException("Access denied")
        }
    }

    override suspend fun getProduct(productId: String): Product {
        val product =
            productRepository.findById(productId).map { it.toDto() }
                .awaitSingleOrNull()
                ?: throw ProductNotFoundException(productId)
        val category =
            categoryRepository.findById(product.categoryId).awaitSingleOrNull()
                ?: throw CategoryNotFoundException(product.categoryId)

        return product.apply { categoryName = category.name }
    }

    override suspend fun deleteProduct(productId: String) {
        productRepository.findById(productId).awaitSingleOrNull()
            ?.let { productRepository.deleteById(it.id!!).awaitSingleOrNull() }
            ?: throw ProductNotFoundException(productId)
    }

    override suspend fun getProductsByCategory(
        categoryId: String,
        active: Boolean,
        query: String,
        pageable: Pageable,
    ): Pair<List<Product>, Long> {
        val products =
            productRepository
                .findAllByCategoryIdAndActiveAndNameLikeIgnoreCase(categoryId, active, query, pageable)
                .map { it.toDto() }
                .asFlow()
                .toList()

        val productCount =
            productRepository
                .countAllByCategoryIdAndActiveAndNameLikeIgnoreCase(categoryId, active, query)
                .awaitSingle()

        return products to productCount
    }

    override suspend fun getProductsByName(
        active: Boolean,
        query: String,
        pageable: Pageable,
    ): Pair<List<Product>, Long> {
        val products =
            productRepository
                .findAllByActiveAndNameLikeIgnoreCase(active, query, pageable)
                .map { it.toDto() }
                .asFlow()
                .toList()

        val productCount =
            productRepository
                .countAllByActiveAndNameLikeIgnoreCase(active, query)
                .awaitSingle()

        return products to productCount
    }

    override suspend fun getProducts(
        pageable: Pageable,
        query: String,
    ): Page<Product> {
        val productsTuple =
            productRepository
                .findByNameLikeIgnoreCase(query, pageable)
                .map { it.toDto() }
                .collectList()
                .zipWith(productRepository.countByNameLikeIgnoreCase(query))
                .awaitFirst()

        val products = productsTuple.t1
        val productsCount = productsTuple.t2

        val categoryIds = HashSet<String>()

        products.mapNotNull { it.categoryId }.forEach { categoryIds.add(it) }

        val categories =
            if (categoryIds.isNotEmpty()) {
                categoryRepository.findAllById(categoryIds).collectList().awaitFirst()
            } else {
                emptyList()
            }

        if (categories.isNotEmpty()) {
            products.map { product ->
                product.apply {
                    categoryName = categories.find { category -> category.id == product.categoryId }?.name
                        ?: throw CategoryNotFoundException(product.categoryId)
                }
            }
        }
        return PageImpl(products, pageable, productsCount)
    }

    private suspend fun uploadProductImage(filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()
        return storageService.uploadProductImage(filePart.filename(), contentType, contentBytes)
    }
}
