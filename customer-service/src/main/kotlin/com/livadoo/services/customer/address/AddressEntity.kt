package com.livadoo.services.customer.address

import com.livadoo.services.customer.address.data.Address
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("addresses")
data class AddressEntity(
    var customerId: String,
    var countryCode: String,
    var fullName: String,
    var phoneNumber: String,
    var address: String,
    var city: String,
    var region: String,
    var isDefault: Boolean,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant? = null,
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0
)

fun AddressEntity.toDto() = Address(
    countryCode, fullName, phoneNumber,
    address, city, region, isDefault, id!!
)