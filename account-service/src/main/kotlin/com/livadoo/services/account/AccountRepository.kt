package com.livadoo.services.account

import com.livadoo.utils.user.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AccountRepository : CoroutineCrudRepository<UserEntity, String> {
    suspend fun findByEmailIgnoreCase(email: String): UserEntity?
}
