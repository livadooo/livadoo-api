package com.livadoo.services.account

import com.livadoo.utils.user.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AuthRepository : CoroutineCrudRepository<UserEntity, String>
