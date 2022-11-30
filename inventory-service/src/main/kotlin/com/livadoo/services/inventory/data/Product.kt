package com.livadoo.services.inventory.data

data class Product(
    val name: String,
    val description: String,
    val categoryId: String,
    val currency: Currency,
    val quantity: Int,
    val price: Float,
    val discountPrice: Float? = null,
    val coverPictureId: String,
    val ordersCount: Int,
    val picturesIds: List<String>,
    val productId: String
)

enum class Currency {
    CFA, USD, EURO
}