package com.livadoo.services.role

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/roles")
class RoleController(
    private val roleService: RoleService,
) {
    @PostMapping
    suspend fun createRole(
        @RequestBody @Validated roleCreate: RoleCreate,
    ): RoleDto {
        return roleService.createRole(roleCreate)
    }

    @PutMapping
    suspend fun updateRole(
        @RequestBody @Validated roleUpdate: RoleUpdate,
    ): RoleDto {
        return roleService.updateRole(roleUpdate)
    }
}
