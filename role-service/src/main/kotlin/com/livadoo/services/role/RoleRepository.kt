package com.livadoo.services.role

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RoleRepository : CoroutineCrudRepository<RoleEntity, String> {
    fun findByRoleIn(roles: List<String>): Flow<RoleEntity>
}
