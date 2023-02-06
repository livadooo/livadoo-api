package com.livadoo.services.inventory.services

import com.livadoo.services.inventory.data.Product
import com.livadoo.services.inventory.data.ProductCreate
import com.livadoo.services.inventory.data.ProductEdit
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart

interface ProductService {

    suspend fun createProduct(productCreate: ProductCreate, filePart: FilePart): Product

    suspend fun updateProduct(productEdit: ProductEdit): Product

    suspend fun updateProductPicture(productId: String, filePart: FilePart): String

    suspend fun getProduct(productId: String): Product

    suspend fun deleteProduct(productId: String)

    suspend fun getProductsByCategory(categoryId: String, active: Boolean, query: String, pageable: Pageable): Pair<List<Product>, Long>

    suspend fun getProductsByName(active: Boolean, query: String, pageable: Pageable): Pair<List<Product>, Long>

    suspend fun getProducts(pageable: Pageable, query: String): Page<Product>
}