package com.livadoo.services.auth

import com.livadoo.utils.user.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AuthRepository : CoroutineCrudRepository<UserEntity, String> {
    suspend fun findByEmailIgnoreCase(email: String): UserEntity?
}
