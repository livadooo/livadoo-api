package com.livadoo.services.permission

import com.livadoo.proxy.permission.PermissionServiceProxy
import org.springframework.stereotype.Service

@Service
class DefaultPermissionServiceProxy(
    private val permissionService: PermissionService,
) : PermissionServiceProxy {
    override suspend fun getBasePermissionsByRoles(roleIds: List<String>): List<String> {
        return permissionService.getBasePermissionsByRoles(roleIds)
    }
}
