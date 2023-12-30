package com.livadoo.services.inventory.services.mongodb.entity

import com.livadoo.services.inventory.data.Product
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("products")
data class ProductEntity(
    var name: String,
    var description: String,
    var categoryId: String,
    var quantity: Int,
    var price: Float,
    var active: Boolean,
    var pictureUrl: String,
    var ordersCount: Int = 0,
    var slideshowPictureUrls: Map<Int, String> = mapOf(),
    var discountPrice: Float? = null,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant? = null,
    var createdBy: String = "",
    var updatedBy: String = "",
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0
)

fun ProductEntity.toDto() = Product(
    name, description, categoryId, quantity, price, active,null,
    discountPrice, pictureUrl, ordersCount,
    slideshowPictureUrls.values.toList(), id!!, createdAt, updatedAt
)