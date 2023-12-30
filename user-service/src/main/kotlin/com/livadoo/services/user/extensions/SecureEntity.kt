package com.livadoo.services.user.extensions

import com.livadoo.services.user.services.mongodb.entity.SecureKeyEntity
import com.livadoo.services.user.services.mongodb.repository.SecureKeyRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.dao.DuplicateKeyException
import java.time.Instant

suspend fun SecureKeyEntity.saveOrUpdate(repository: SecureKeyRepository, userId: String, key: String, expiresAt: Instant) {
    try {
        repository.save(this).awaitSingle()
    } catch (exception: DuplicateKeyException) {
        val existingActivationKey = repository.findByUserId(userId).awaitSingle()
        existingActivationKey.apply {
            updatedAt = Instant.now()
            this.key = key
            expirationDate = expiresAt
        }
        repository.save(existingActivationKey).awaitSingle()
    }
}