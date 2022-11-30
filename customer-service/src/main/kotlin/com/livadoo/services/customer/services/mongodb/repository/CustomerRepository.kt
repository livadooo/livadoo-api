package com.livadoo.services.customer.services.mongodb.repository

import com.livadoo.services.customer.services.mongodb.entity.CustomerEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomerRepository : ReactiveCrudRepository<CustomerEntity, String> {

    fun findByUserId(userId: String): Mono<CustomerEntity>

    fun findByCustomerId(customerId: String): Mono<CustomerEntity>

    @Query("{}")
    fun findCustomers(pageable: Pageable): Flux<CustomerEntity>
}