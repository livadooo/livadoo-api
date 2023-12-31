package com.livadoo.services.role

import com.livadoo.services.role.exception.DuplicateRoleException
import com.livadoo.services.role.exception.DuplicateRoleTitleException
import com.livadoo.services.role.exception.RoleNotFoundException
import com.livadoo.shared.extension.containsExceptionKey
import com.livadoo.utils.exception.InternalErrorException
import com.livadoo.utils.security.config.AppSecurityContext
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class DefaultRoleService(
    private val roleRepository: RoleRepository,
    private val clock: Clock,
    private val securityContext: AppSecurityContext,
) : RoleService {
    override suspend fun createRole(roleCreate: RoleCreate): RoleDto {
        val roleEntity =
            RoleEntity(
                role = roleCreate.role,
                title = roleCreate.title,
                description = roleCreate.description,
                createdAt = clock.instant(),
                createdBy = securityContext.getCurrentUserId(),
            )
        return handleSave(roleEntity).toDto()
    }

    override suspend fun updateRole(roleUpdate: RoleUpdate): RoleDto {
        val roleEntity =
            roleRepository.findById(roleUpdate.roleId)
                ?: throw RoleNotFoundException(roleUpdate.roleId)

        roleEntity.apply {
            title = roleUpdate.title
            description = roleUpdate.description
            updatedAt = clock.instant()
            updatedBy = securityContext.getCurrentUserId()
        }
        return handleSave(roleEntity).toDto()
    }

    private suspend fun handleSave(roleEntity: RoleEntity): RoleEntity {
        return try {
            roleRepository.save(roleEntity)
        } catch (exception: DuplicateKeyException) {
            if (exception.message!!.containsExceptionKey("role")) {
                throw DuplicateRoleException(roleEntity.role)
            } else if (exception.message!!.containsExceptionKey("title")) {
                throw DuplicateRoleTitleException(roleEntity.title)
            } else {
                throw InternalErrorException()
            }
        }
    }
}
