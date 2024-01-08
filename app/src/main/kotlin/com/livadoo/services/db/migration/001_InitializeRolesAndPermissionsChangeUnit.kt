package com.livadoo.services.db.migration

import com.livadoo.services.permission.PermissionEntity
import com.livadoo.services.permission.PermissionRepository
import com.livadoo.services.permission.buildPermissionId
import com.livadoo.services.role.RoleEntity
import com.livadoo.services.role.RoleRepository
import com.livadoo.services.role.buildRoleId
import com.livadoo.utils.security.domain.SYSTEM_ACCOUNT
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync
import kotlinx.coroutines.reactor.mono
import java.time.Clock

@ChangeUnit(id = "initialize-roles-and-permissions", order = "001")
class InitializeRolesAndPermissionsChangeUnit {
    private val subscriberSync = MongoSubscriberSync<Unit>()

    @Execution
    fun execution(
        roleRepository: RoleRepository,
        permissionRepository: PermissionRepository,
        clock: Clock,
    ) {
        mono {
            var roleEntity = RoleEntity(
                roleId = buildRoleId,
                role = "ROLE_OWNER",
                title = "Owner",
                description = "Manages all aspects in the system",
                createdAt = clock.instant(),
                createdBy = SYSTEM_ACCOUNT,
            )
            roleEntity = roleRepository.save(roleEntity)
            val permissionEntity = PermissionEntity(
                permissionId = buildPermissionId,
                roleId = roleEntity.roleId,
                permission = "customers:list",
                description = "Accesses all customers accounts",
                createdAt = clock.instant(),
                createdBy = SYSTEM_ACCOUNT,
            )
            permissionRepository.save(permissionEntity)
            Unit
        }.subscribe(subscriberSync)
        subscriberSync.await()
    }

    @RollbackExecution
    fun rollbackExecution() = Unit
}
