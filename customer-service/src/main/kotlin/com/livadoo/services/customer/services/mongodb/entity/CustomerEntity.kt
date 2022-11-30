package com.livadoo.services.customer.services.mongodb.entity

import com.livadoo.services.customer.data.Customer
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("customers")
data class CustomerEntity(
    @Indexed(unique = true)
    var userId: String,
    @Indexed(unique = true)
    var customerId: String,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant? = null,
    var createdBy: String = "",
    var updatedBy: String = "",
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0
)

fun CustomerEntity.toDto() = Customer(userId, customerId)
