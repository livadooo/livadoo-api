package com.livadoo.services.inventory.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.livadoo.services.inventory.data.Product
import com.livadoo.services.inventory.data.ProductCreate
import com.livadoo.services.inventory.data.ProductEdit
import com.livadoo.services.inventory.services.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/products")
class ProductController @Autowired constructor(
    private val productService: ProductService
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun createProduct(
        @RequestPart("product") product: String,
        @RequestPart("image") filePart: FilePart,
    ) {
        val productCreate = ObjectMapper().readValue(product, ProductCreate::class.java)
        productService.createProduct(productCreate, filePart)
    }

    @PutMapping
    suspend fun updateProduct(@RequestBody productEdit: ProductEdit) {
        productService.updateProduct(productEdit)
    }

    @PutMapping("/{productId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateProductCoverPicture(
        @PathVariable productId: String,
        @RequestPart("picture") filePart: FilePart
    ) {
        productService.updateProductCoverPicture(productId, filePart)
    }

    @GetMapping("/{productId}")
    suspend fun getProduct(@PathVariable productId: String): Product {
        return productService.getProduct(productId)
    }

    @DeleteMapping("/{productId}")
    suspend fun deleteProduct(@PathVariable productId: String) {
        productService.deleteProduct(productId)
    }

    @GetMapping("/category/{categoryId}")
    suspend fun getProductsByCategory(
        @RequestParam("page", required = false) page: Int?,
        @RequestParam("size", required = false) size: Int?,
        @RequestParam("active", required = false) active: Boolean?,
        @RequestParam("query", required = false) query: String?,
        @PathVariable categoryId: String
    ): ResponseEntity<List<Product>> {
        val pageable = PageRequest.of(page ?: 0, size ?: 100)
        val (products, productsCount) = productService.getProductsByCategory(categoryId, active ?: true, query ?: "", pageable)
        return ResponseEntity.ok()
            .header("X-Total-Count", "$productsCount")
            .body(products)
    }

    @GetMapping
    suspend fun getProductsByName(
        @RequestParam("page", required = false) page: Int?,
        @RequestParam("size", required = false) size: Int?,
        @RequestParam("active", required = false) active: Boolean?,
        @RequestParam("query", required = false) query: String?
    ): ResponseEntity<List<Product>> {
        val pageable = PageRequest.of(page ?: 0, size ?: 100)
        val (products, productsCount) = productService.getProductsByName(active ?: true, query ?: "", pageable)
        return ResponseEntity.ok()
            .header("X-Total-Count", "$productsCount")
            .body(products)
    }
}