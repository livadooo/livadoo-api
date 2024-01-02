package com.livadoo.proxy.role

interface RoleServiceProxy {
    suspend fun getRolesByIds(roleIds: List<String>): List<String>
}
