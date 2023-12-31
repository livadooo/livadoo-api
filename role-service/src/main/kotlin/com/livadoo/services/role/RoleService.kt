package com.livadoo.services.role

interface RoleService {
    suspend fun createRole(roleCreate: RoleCreate): RoleDto

    suspend fun updateRole(roleUpdate: RoleUpdate): RoleDto
}
