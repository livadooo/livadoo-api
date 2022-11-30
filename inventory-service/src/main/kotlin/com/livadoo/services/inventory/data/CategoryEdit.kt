package com.livadoo.services.inventory.data

data class CategoryEdit(
    val name: String,
    val description: String,
    val parentId: String?,
    val active: Boolean,
    val categoryId: String
)