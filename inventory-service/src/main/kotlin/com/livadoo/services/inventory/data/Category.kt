package com.livadoo.services.inventory.data

data class Category(
    val name: String,
    val description: String,
    val parentId: String?,
    val pictureId: String,
    val active: Boolean,
    val categoryId: String
)