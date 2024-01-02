package com.livadoo.services.notification.email

import com.livadoo.services.notification.EmailType

interface EmailSender {
    fun send(recipient: String, subject: String, body: String, emailType: EmailType)
}
