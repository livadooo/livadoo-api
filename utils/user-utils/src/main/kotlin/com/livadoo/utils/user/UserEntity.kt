package com.livadoo.utils.user

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "users")
data class UserEntity(
    var firstName: String,
    var lastName: String,
    var language: Language,
    @Indexed(unique = true)
    var userId: String,
    @Indexed(unique = true, sparse = true)
    var phoneNumber: String?,
    @Indexed(unique = true, sparse = true)
    var email: String?,
    var roleIds: List<String>,
    var permissionIds: List<String>,
    var activated: Boolean,
    var password: String?,
    var photoUrl: String? = null,
    var address: String? = null,
    var city: String? = null,
    var country: String? = null,
    var verified: Boolean = false,
    var blocked: Boolean = false,
    var deleted: Boolean = false,
    var createdBy: String? = null,
    var createdAt: Instant? = null,
    var updatedBy: String? = null,
    var updatedAt: Instant? = null,
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0,
)

fun UserEntity.toDto() = UserDto(
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    roles = roleIds,
    permissions = permissionIds,
    email = email,
    photoUrl = photoUrl,
    address = address,
    city = city,
    country = country,
    verified = verified,
    blocked = blocked,
    deleted = deleted,
    createdBy = createdBy,
    createdAt = createdAt,
    updatedBy = updatedBy,
    updatedAt = updatedAt,
    userId = userId,
    language = language,
    activated = activated,
)
