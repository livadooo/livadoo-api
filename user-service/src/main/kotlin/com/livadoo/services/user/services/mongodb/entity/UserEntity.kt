package com.livadoo.services.user.services.mongodb.entity

import com.livadoo.services.user.data.User
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "users")
data class UserEntity(
    var firstName: String,
    var lastName: String,
    @Indexed(unique = true)
    var phoneNumber: String,
    val authority: String,
    var password: String,
    @Indexed(unique = true)
    var email: String,
    var portraitUrl: String? = null,
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
    var version: Int = 0
)


fun UserEntity.toDto() = User(
    firstName, lastName, phoneNumber, authority, email,
    portraitUrl, address, city, country, verified, blocked, deleted, createdBy,
    createdAt, updatedBy, updatedAt, id
)
