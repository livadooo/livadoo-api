package com.livadoo.services.role

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RoleRepository : CoroutineCrudRepository<RoleEntity, String>
