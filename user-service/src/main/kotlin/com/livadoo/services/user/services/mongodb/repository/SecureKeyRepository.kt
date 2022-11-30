package com.livadoo.services.user.services.mongodb.repository

import com.livadoo.services.user.services.mongodb.entity.SecureKeyEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface SecureKeyRepository : ReactiveCrudRepository<SecureKeyEntity, String> {

    fun findByUserId(userId: String): Mono<SecureKeyEntity>

    fun findByKey(key: String): Mono<SecureKeyEntity>
}