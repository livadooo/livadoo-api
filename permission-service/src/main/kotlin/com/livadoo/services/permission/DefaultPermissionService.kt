package com.livadoo.services.permission

import com.livadoo.services.permission.exception.DuplicatePermissionException
import com.livadoo.services.permission.exception.PermissionNotFoundException
import com.livadoo.shared.extension.containsExceptionKey
import com.livadoo.utils.exception.InternalErrorException
import com.livadoo.utils.security.config.AppSecurityContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class DefaultPermissionService(
    private val permissionRepository: PermissionRepository,
    private val clock: Clock,
    private val securityContext: AppSecurityContext,
) : PermissionService {
    override suspend fun createPermission(permissionCreate: PermissionCreate): PermissionDto {
        val permissionEntity = PermissionEntity(
            permission = permissionCreate.permission,
            roleId = permissionCreate.roleId,
            description = permissionCreate.description,
            base = permissionCreate.isBase,
            createdAt = clock.instant(),
            createdBy = securityContext.getCurrentUserId(),
        )
        return handleSave(permissionEntity).toDto()
    }

    override suspend fun updatePermission(permissionUpdate: PermissionUpdate): PermissionDto {
        val permissionEntity = permissionRepository.findById(permissionUpdate.permissionId)
            ?: throw PermissionNotFoundException(permissionUpdate.permissionId)

        permissionEntity.apply {
            description = permissionUpdate.description
            updatedAt = clock.instant()
            updatedBy = securityContext.getCurrentUserId()
        }
        return handleSave(permissionEntity).toDto()
    }

    override suspend fun getBasePermissionsByRoles(roleIds: List<String>): List<String> {
        return permissionRepository.findByBaseIsTrueAndRoleIdIn(roleIds)
            .map { it.id!! }
            .toList()
    }

    private suspend fun handleSave(permissionEntity: PermissionEntity): PermissionEntity {
        return try {
            permissionRepository.save(permissionEntity)
        } catch (exception: DuplicateKeyException) {
            if (exception.message!!.containsExceptionKey("permission")) {
                throw DuplicatePermissionException(permissionEntity.permission)
            } else {
                throw InternalErrorException()
            }
        }
    }
}
