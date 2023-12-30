package com.livadoo.services.inventory.services.mongodb.repository

import com.livadoo.services.inventory.services.mongodb.entity.CategoryEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CategoryRepository : ReactiveCrudRepository<CategoryEntity, String> {

    fun findAllByParentIdAndActive(parentId: String?, active: Boolean, pageable: Pageable): Flux<CategoryEntity>

    fun findByNameLikeIgnoreCase(query: String, pageable: Pageable): Flux<CategoryEntity>

    fun countByNameLikeIgnoreCase(query: String): Mono<Long>

    fun countAllByParentIdAndActive(parentId: String?, active: Boolean): Mono<Long>
}