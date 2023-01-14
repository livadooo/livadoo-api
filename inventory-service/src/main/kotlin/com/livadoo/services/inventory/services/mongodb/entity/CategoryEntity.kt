package com.livadoo.services.inventory.services.mongodb.entity

import com.livadoo.services.inventory.data.Category
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("categories")
data class CategoryEntity(
    var name: String,
    var description: String,
    var parentId: String?,
    var pictureUrl: String,
    var active: Boolean,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant? = null,
    var createdBy: String = "",
    var updatedBy: String = "",
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0
)

fun CategoryEntity.toDto(): Category = Category(name, description, parentId, pictureUrl, active, id!!)