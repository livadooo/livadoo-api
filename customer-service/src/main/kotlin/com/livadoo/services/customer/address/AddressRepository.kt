package com.livadoo.services.customer.address

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface AddressRepository : ReactiveCrudRepository<AddressEntity, String> {

    fun findByCustomerId(customerId: String): Flux<AddressEntity>
}