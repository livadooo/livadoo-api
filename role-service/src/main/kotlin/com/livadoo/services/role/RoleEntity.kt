package com.livadoo.services.role

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("roles")
data class RoleEntity(
    @Indexed(unique = true)
    var role: String,
    @Indexed(unique = true)
    var roleId: String,
    @Indexed(unique = true)
    var title: String,
    var description: String,
    var createdAt: Instant,
    var updatedAt: Instant? = null,
    var createdBy: String,
    var updatedBy: String? = null,
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0,
)

fun RoleEntity.toDto(): RoleDto =
    RoleDto(
        role = role,
        title = title,
        description = description,
        roleId = roleId,
    )
