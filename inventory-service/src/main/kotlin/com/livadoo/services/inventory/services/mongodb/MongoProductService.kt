package com.livadoo.services.inventory.services.mongodb

import com.livadoo.services.common.exceptions.NotAllowedException
import com.livadoo.services.common.exceptions.NotAuthenticatedException
import com.livadoo.services.common.utils.extractContent
import com.livadoo.library.security.utils.currentAuthUser
import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.inventory.data.Product
import com.livadoo.services.inventory.data.ProductCreate
import com.livadoo.services.inventory.data.ProductEdit
import com.livadoo.services.inventory.exceptions.ProductNotFoundException
import com.livadoo.services.inventory.services.ProductService
import com.livadoo.services.inventory.services.mongodb.entity.ProductEntity
import com.livadoo.services.inventory.services.mongodb.entity.toDto
import com.livadoo.services.inventory.services.mongodb.repository.ProductRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class MongoProductService @Autowired constructor(
    private val productRepository: ProductRepository,
    private val storageService: StorageServiceProxy,
) : ProductService {

    override suspend fun createProduct(productCreate: ProductCreate, filePart: FilePart): Product {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw NotAuthenticatedException()

        return if (currentUser.isAdmin) {
            val coverPictureUrl = uploadFile(filePart)
            val (name, description, categoryId, quantity, price) = productCreate
            val productEntity = ProductEntity(name, description, categoryId, quantity, price, coverPictureUrl, createdBy = currentUser.username)

            productRepository.save(productEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw NotAllowedException()
        }
    }

    override suspend fun updateProduct(productEdit: ProductEdit): Product {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw NotAuthenticatedException()

        return if (currentUser.isAdmin) {
            val (name, description, categoryId, currency, quantity, price, productId, discountPrice) = productEdit

            val productEntity = productRepository.findById(productId).awaitSingleOrNull()
                ?.apply {
                    this.name = name
                    this.description = description
                    this.categoryId = categoryId
                    this.currency = currency
                    this.quantity = quantity
                    this.price = price
                    this.discountPrice = discountPrice
                }
                ?: throw ProductNotFoundException(productId)

            productRepository.save(productEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw NotAllowedException()
        }
    }

    override suspend fun updateProductCoverPicture(productId: String, filePart: FilePart): Product {
        val currentUser = currentAuthUser.awaitSingleOrNull() ?: throw NotAuthenticatedException()

        return if (currentUser.isAdmin) {
            val productEntity = productRepository.findById(productId).awaitSingleOrNull()
                ?: throw ProductNotFoundException(productId)

            productEntity.pictureUrl = uploadFile(filePart)

            productRepository.save(productEntity).map { it.toDto() }.awaitSingle()
        } else {
            throw NotAllowedException()
        }
    }

    override suspend fun getProduct(productId: String): Product {
        return productRepository.findById(productId).map { it.toDto() }
            .awaitSingleOrNull()
            ?: throw ProductNotFoundException(productId)
    }

    override suspend fun deleteProduct(productId: String) {
        productRepository.findById(productId).awaitSingleOrNull()
            ?.let { productRepository.deleteById(it.id!!).awaitSingleOrNull() }
            ?: throw ProductNotFoundException(productId)
    }

    override suspend fun getProductsByCategory(categoryId: String, active: Boolean, query: String, pageable: Pageable): Pair<List<Product>, Long> {
        val products = productRepository
            .findAllByCategoryIdAndActiveAndNameLikeIgnoreCase(categoryId, active, query, pageable)
            .map { it.toDto() }
            .asFlow()
            .toList()

        val productCount = productRepository
            .countAllByCategoryIdAndActiveAndNameLikeIgnoreCase(categoryId, active, query)
            .awaitSingle()

        return products to productCount
    }

    override suspend fun getProductsByName(active: Boolean, query: String, pageable: Pageable): Pair<List<Product>, Long> {
        val products = productRepository
            .findAllByActiveAndNameLikeIgnoreCase(active, query, pageable)
            .map { it.toDto() }
            .asFlow()
            .toList()

        val productCount = productRepository
            .countAllByActiveAndNameLikeIgnoreCase(active, query)
            .awaitSingle()

        return products to productCount
    }

    private suspend fun uploadFile(filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()
        return storageService.uploadProductImage(filePart.filename(), contentType, contentBytes)
    }
}