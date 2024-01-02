package com.livadoo.services.notification.email

interface EmailSenderFactory {
    fun getSender(): EmailSender
}
