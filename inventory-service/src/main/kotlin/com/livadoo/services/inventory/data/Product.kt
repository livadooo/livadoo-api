package com.livadoo.services.inventory.data

data class Product(
    val name: String,
    val description: String,
    val categoryId: String,
    val currency: Currency,
    val quantity: Int,
    val price: Float,
    val discountPrice: Float? = null,
    val pictureUrl: String,
    val ordersCount: Int,
    val slideshowPictureUrls: Map<Int, String>,
    val productId: String
)

enum class Currency {
    CFA, USD, EURO
}