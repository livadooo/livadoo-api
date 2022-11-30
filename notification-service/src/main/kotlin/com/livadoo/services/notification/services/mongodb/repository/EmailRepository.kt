package com.livadoo.services.notification.services.mongodb.repository

import com.livadoo.services.notification.data.DeliveryStatus
import com.livadoo.services.notification.services.mongodb.entity.EmailEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface EmailRepository : ReactiveCrudRepository<EmailEntity, String> {

    fun findByUserId(userId: String): Flux<EmailEntity>

    fun findByStatus(status: DeliveryStatus): Flux<EmailEntity>
}