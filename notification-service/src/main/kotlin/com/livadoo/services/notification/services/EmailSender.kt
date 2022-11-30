package com.livadoo.services.notification.services

import com.livadoo.services.notification.data.DeliveryStatus


interface EmailSender {

    suspend fun send(userId: String, email: String, subject: String, body: String): DeliveryStatus
}