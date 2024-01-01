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
    @Indexed(unique = true)
    var phoneNumber: String,
    var authority: String,
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
    var version: Int = 0,
)

fun UserEntity.toDto() =
    UserDto(
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        authority = authority,
        email = email,
        portraitUrl = portraitUrl,
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
        userId = id,
    )
