package com.livadoo.services.user.services

import com.livadoo.utils.user.UserEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository : ReactiveCrudRepository<UserEntity, String> {
    fun findByPhoneNumber(phoneNumber: String): Mono<UserEntity>

    fun findByEmailIgnoreCase(email: String): Mono<UserEntity>

    fun findAllByIdIn(id: List<String>): Flux<UserEntity>
}
