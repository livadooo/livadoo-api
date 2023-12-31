package com.livadoo.services.permission

interface PermissionService {
    suspend fun createPermission(permissionCreate: PermissionCreate): PermissionDto

    suspend fun updatePermission(permissionUpdate: PermissionUpdate): PermissionDto
}
