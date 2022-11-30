package com.livadoo.services.user.data

import com.livadoo.services.user.services.mongodb.entity.KeyType
import java.time.Instant

data class SecureKey(
    val key: String,
    val userId: String,
    val keyType: KeyType,
    var expirationDate: Instant,
    val createdAt: Instant,
    var updatedAt: Instant,
    val keyId: String
)