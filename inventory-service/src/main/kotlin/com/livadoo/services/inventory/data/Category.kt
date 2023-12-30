package com.livadoo.services.inventory.data

import java.time.Instant

data class Category(
    val name: String,
    val description: String,
    val parentId: String?,
    var parentName: String?,
    val pictureUrl: String,
    val active: Boolean,
    val categoryId: String,
    val createdAt: Instant,
    val updatedAt: Instant?,
)