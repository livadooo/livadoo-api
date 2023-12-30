package com.livadoo.services.inventory.data

import java.time.Instant

data class Product(
    val name: String,
    val description: String,
    val categoryId: String,
    val quantity: Int,
    val price: Float,
    val active: Boolean,
    var categoryName: String?,
    val discountPrice: Float? = null,
    val pictureUrl: String,
    val ordersCount: Int,
    val slideshowPictureUrls: List<String>,
    val productId: String,
    val createdAt: Instant,
    val updatedAt: Instant?,
)

enum class Currency {
    CFA, USD, EURO
}