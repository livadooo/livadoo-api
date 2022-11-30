package com.livadoo.services.storage.services.mongodb.repository

import com.livadoo.services.storage.services.mongodb.entity.DocumentEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DocumentRepository : ReactiveCrudRepository<DocumentEntity, String> {

    fun findByUuid(uuid: String): Mono<DocumentEntity>

    fun findByUriContains(keyword: String): Flux<DocumentEntity>
}
