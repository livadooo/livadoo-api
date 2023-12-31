package com.livadoo.services.permission

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/permissions")
class PermissionController(
    private val permissionService: PermissionService,
) {
    @PostMapping
    suspend fun createPermission(
        @RequestBody @Validated permissionCreate: PermissionCreate,
    ): PermissionDto {
        return permissionService.createPermission(permissionCreate)
    }

    @PutMapping
    suspend fun updatePermission(
        @RequestBody @Validated permissionUpdate: PermissionUpdate,
    ): PermissionDto {
        return permissionService.updatePermission(permissionUpdate)
    }
}
