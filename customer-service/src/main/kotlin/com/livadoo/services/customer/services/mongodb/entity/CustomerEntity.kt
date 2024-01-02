package com.livadoo.services.customer.services.mongodb.entity

import com.livadoo.services.customer.data.CustomerDto
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("customers")
data class CustomerEntity(
    @Indexed(unique = true)
    var customerId: String,
    @Indexed(unique = true)
    var userId: String,
    var createdAt: Instant,
    var updatedAt: Instant? = null,
    var createdBy: String = "",
    var updatedBy: String = "",
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0,
)

fun CustomerEntity.toDto() = CustomerDto(userId, customerId)
