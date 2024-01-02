package com.livadoo.services.permission

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("permissions")
data class PermissionEntity(
    @Indexed(unique = true)
    var permission: String,
    var description: String,
    val roleId: String,
    var createdAt: Instant,
    var base: Boolean,
    var updatedAt: Instant? = null,
    var createdBy: String,
    var updatedBy: String? = null,
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0,
)

fun PermissionEntity.toDto(): PermissionDto = PermissionDto(
    permission = permission,
    description = description,
    permissionId = id!!,
)
