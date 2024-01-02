package com.livadoo.services.notification.email

import com.livadoo.services.notification.MailProvider.SENDGRID
import com.livadoo.services.notification.MailProvider.SMTP
import com.livadoo.services.notification.MailProvider.ZEPTOMAIL
import com.livadoo.services.notification.config.NotificationProperties
import com.livadoo.services.notification.email.sender.SendgridEmailSender
import com.livadoo.services.notification.email.sender.SmtpEmailSender
import com.livadoo.services.notification.email.sender.ZeptoMailSender
import org.springframework.stereotype.Service

@Service
class DefaultEmailSenderFactory(
    private val notificationProperties: NotificationProperties,
    private val smtpEmailSender: SmtpEmailSender,
    private val zeptoMailSender: ZeptoMailSender,
    private val sendgridEmailSender: SendgridEmailSender,
) : EmailSenderFactory {
    override fun getSender(): EmailSender {
        return when (notificationProperties.mail.activeProvider) {
            SMTP -> smtpEmailSender
            ZEPTOMAIL -> zeptoMailSender
            SENDGRID -> sendgridEmailSender
        }
    }
}
