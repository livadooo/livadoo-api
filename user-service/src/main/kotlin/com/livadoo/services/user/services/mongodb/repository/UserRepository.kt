package com.livadoo.services.user.services.mongodb.repository

import com.livadoo.services.user.services.mongodb.entity.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface UserRepository : ReactiveCrudRepository<UserEntity, String> {

    fun findByPhoneNumber(phoneNumber: String): Mono<UserEntity>

    fun findByEmailIgnoreCase(email: String): Mono<UserEntity>

    @Query(SEARCH_USERS)
    fun findUsersByAuthorityAndFirstNameOrLastnameOrEmailOrPhoneNumber(
        authority: String,
        query: String,
        pageable: Pageable
    ): Flux<UserEntity>

    @Query(SEARCH_USERS, count = true)
    fun countUsersByAuthorityAndFirstNameOrLastnameOrEmailOrPhoneNumber(
        authority: String,
        query: String
    ): Mono<Long>

    fun findAllByIdIn(id: List<String>): Flux<UserEntity>
}

private const val SEARCH_USERS = "{\$and: [{authority: ?0}, {\$or: [{firstName : {\$regex : ?1, \$options : i }}, {lastName : {\$regex : ?1, \$options : i }}, {email : {\$regex : ?1, \$options : i }}, {phoneNumber : {\$regex : ?1 }}]}]}"