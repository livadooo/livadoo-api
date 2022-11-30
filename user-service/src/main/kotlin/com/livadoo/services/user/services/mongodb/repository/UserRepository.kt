package com.livadoo.services.user.services.mongodb.repository

import com.livadoo.services.user.services.mongodb.entity.AuthorityEntity
import com.livadoo.services.user.services.mongodb.entity.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface UserRepository : ReactiveCrudRepository<UserEntity, String> {

    fun findByPhoneNumber(phoneNumber: String): Mono<UserEntity>

    fun findByEmailIgnoreCase(email: String): Mono<UserEntity>

    @Query("{ isDeleted: false }")
    fun findAll(pageable: Pageable): Flux<UserEntity>

    fun findByAuthoritiesContainingAndDeletedFalse(
        authorities: List<AuthorityEntity>,
        pageable: Pageable
    ): Flux<UserEntity>

    fun countAllByAuthoritiesContainingAndDeletedFalse(authorities: List<AuthorityEntity>): Mono<Long>

    fun findAllByIdIn(id: List<String>): Flux<UserEntity>
}
