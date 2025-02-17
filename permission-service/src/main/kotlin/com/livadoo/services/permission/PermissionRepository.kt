package com.livadoo.services.permission

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PermissionRepository : CoroutineCrudRepository<PermissionEntity, String> {
    fun findByRoleIdIn(roleIds: List<String>): Flow<PermissionEntity>
}
