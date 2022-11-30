package com.livadoo.services.notification.services.mongodb.entity

import com.livadoo.services.notification.data.DeliveryStatus
import com.livadoo.services.notification.data.EmailType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "emails")
data class EmailEntity(
    var userId: String,
    var status: DeliveryStatus,
    var type: EmailType,
    var sentAt: Instant = Instant.now(),
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0
)