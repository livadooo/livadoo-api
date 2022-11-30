package com.livadoo.services.inventory.data

data class ProductEdit(
    val name: String,
    val description: String,
    val categoryId: String,
    val currency: Currency,
    val quantity: Int,
    val price: Float,
    val productId: String,
    val discountPrice: Float? = null
)