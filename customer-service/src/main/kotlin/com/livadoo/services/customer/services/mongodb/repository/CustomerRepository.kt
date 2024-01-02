package com.livadoo.services.customer.services.mongodb.repository

import com.livadoo.services.customer.services.mongodb.entity.CustomerEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CustomerRepository : CoroutineCrudRepository<CustomerEntity, String> {
    suspend fun findByUserId(userId: String): CustomerEntity?

    suspend fun findByCustomerId(customerId: String): CustomerEntity?
}
