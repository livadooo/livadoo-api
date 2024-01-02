package com.livadoo.services.role

import com.livadoo.proxy.role.RoleServiceProxy
import org.springframework.stereotype.Service

@Service
class DefaultRoleServiceProxy(
    private val roleService: RoleService,
) : RoleServiceProxy {
    override suspend fun getRolesByIds(roleIds: List<String>): List<String> {
        return roleService.getRolesByIds(roleIds)
    }
}
