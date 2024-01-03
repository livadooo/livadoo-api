package com.livadoo.services.user.services

import com.livadoo.utils.user.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<UserEntity, String> {
    suspend fun findByUserId(userId: String): UserEntity?

    suspend fun findByPhoneNumber(phoneNumber: String): UserEntity?

    suspend fun findByEmailIgnoreCase(email: String): UserEntity?
}
