package com.livadoo.proxy.permission

interface PermissionServiceProxy {
    suspend fun getBasePermissionsByRoles(roleIds: List<String>): List<String>
}
