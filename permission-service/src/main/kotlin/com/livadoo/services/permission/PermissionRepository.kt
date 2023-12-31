package com.livadoo.services.permission

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PermissionRepository : CoroutineCrudRepository<PermissionEntity, String>
