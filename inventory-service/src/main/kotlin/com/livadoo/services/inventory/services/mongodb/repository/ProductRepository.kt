package com.livadoo.services.inventory.services.mongodb.repository

import com.livadoo.services.inventory.services.mongodb.entity.ProductEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductRepository : ReactiveCrudRepository<ProductEntity, String> {

    fun findByNameLikeIgnoreCase(query: String, pageable: Pageable): Flux<ProductEntity>

    fun findAllByCategoryIdAndActiveAndNameLikeIgnoreCase(categoryId: String, active: Boolean, name: String, pageable: Pageable): Flux<ProductEntity>

    fun findAllByActiveAndNameLikeIgnoreCase(active: Boolean, name: String, pageable: Pageable): Flux<ProductEntity>

    fun countAllByCategoryIdAndActiveAndNameLikeIgnoreCase(categoryId: String, active: Boolean, name: String): Mono<Long>

    fun countAllByActiveAndNameLikeIgnoreCase(active: Boolean, name: String): Mono<Long>

    fun countByNameLikeIgnoreCase(query: String): Mono<Long>
}