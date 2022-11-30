package com.livadoo.services.user.services.mongodb.entity

import com.livadoo.services.user.data.SecureKey
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "secure_keys")
data class SecureKeyEntity(
    var key: String,
    @Indexed(unique = true)
    val userId: String,
    val keyType: KeyType,
    var expirationDate: Instant,
    var createdAt: Instant,
    var updatedAt: Instant,
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0
)

enum class KeyType { ACCOUNT_VERIFY, PASSWORD_RESET }

fun SecureKeyEntity.toDto() = SecureKey(key, userId, keyType, expirationDate, createdAt, updatedAt, id!!)