package com.livadoo.services.inventory.data

data class ProductEdit(
    val name: String,
    val description: String,
    val categoryId: String,
    val quantity: Int,
    val price: Float,
    val active: Boolean,
    val productId: String,
    val discountPrice: Float? = null
)