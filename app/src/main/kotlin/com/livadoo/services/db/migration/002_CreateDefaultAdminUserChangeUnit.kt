package com.livadoo.services.db.migration

import com.livadoo.services.permission.PermissionRepository
import com.livadoo.services.role.RoleRepository
import com.livadoo.services.user.services.UserRepository
import com.livadoo.utils.security.domain.SYSTEM_ACCOUNT
import com.livadoo.utils.user.Language
import com.livadoo.utils.user.UserEntity
import com.livadoo.utils.user.buildUserId
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Clock

@ChangeUnit(id = "create-default-admin-user", order = "002")
class CreateDefaultAdminUserChangeUnit {
    private val subscriberSync = MongoSubscriberSync<UserEntity>()

    @Execution
    fun execution(
        roleRepository: RoleRepository,
        permissionRepository: PermissionRepository,
        passwordEncoder: PasswordEncoder,
        userRepository: UserRepository,
        clock: Clock,
    ) {
        mono {
            val (roleIds, permissionIds) = coroutineScope {
                val roleIdsAsync = async {
                    roleRepository.findByRoleIn(listOf("ROLE_OWNER")).map { it.roleId }.toList()
                }
                val permissionIdsAsync = async {
                    permissionRepository.findAll().map { it.permissionId }.toList()
                }
                roleIdsAsync.await() to permissionIdsAsync.await()
            }

            val userEntity = UserEntity(
                firstName = "Admin",
                lastName = "Admin",
                language = Language.FR,
                userId = buildUserId,
                phoneNumber = "+237670000000",
                email = "developers@livadoo.com",
                activated = true,
                verified = true,
                roleIds = roleIds,
                permissionIds = permissionIds,
                password = passwordEncoder.encode("90{00D0qHj,x"),
                createdAt = clock.instant(),
                createdBy = SYSTEM_ACCOUNT,
            )
            userRepository.save(userEntity)
        }.subscribe(subscriberSync)
    }

    @RollbackExecution
    fun rollbackExecution() = Unit
}
