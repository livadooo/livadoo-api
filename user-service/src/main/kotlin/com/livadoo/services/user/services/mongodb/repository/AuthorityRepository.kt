package com.livadoo.services.user.services.mongodb.repository

import com.livadoo.services.user.services.mongodb.entity.AuthorityEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository


interface AuthorityRepository : ReactiveCrudRepository<AuthorityEntity, String>
