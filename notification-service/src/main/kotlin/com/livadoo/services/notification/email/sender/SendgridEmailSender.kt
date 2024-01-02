package com.livadoo.services.notification.email.sender

import com.livadoo.services.notification.EmailType
import com.livadoo.services.notification.MailUtils.getMailErrorDescription
import com.livadoo.services.notification.MailUtils.getMailSendingDescription
import com.livadoo.services.notification.MailUtils.getMailSuccessDescription
import com.livadoo.services.notification.MailUtils.getSender
import com.livadoo.services.notification.config.NotificationProperties
import com.livadoo.services.notification.email.EmailSender
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException

@Service("sendgrid")
class SendgridEmailSender(
    private val notificationProperties: NotificationProperties,
) : EmailSender {

    private val logger = LoggerFactory.getLogger(SendgridEmailSender::class.java)

    override fun send(recipient: String, subject: String, body: String, emailType: EmailType) {
        val sender = getSender(emailType)
        val mail = Mail(
            Email(sender.address, sender.name),
            subject,
            Email(recipient),
            Content("text/html", body),
        )

        return sendEmail(recipient = recipient, mail = mail, emailType = emailType)
    }

    private fun sendEmail(recipient: String, mail: Mail, emailType: EmailType) {
        val sendgrid = SendGrid(notificationProperties.mail.sendgrid.apiKey)
        val request = Request().apply {
            method = Method.POST
            endpoint = "mail/send"
            this.body = mail.build()
        }

        try {
            logger.info(getMailSendingDescription(emailType = emailType, email = recipient))
            sendgrid.api(request)
            logger.info(getMailSuccessDescription(emailType = emailType, email = recipient))
        } catch (exception: IOException) {
            logger.error(getMailErrorDescription(emailType = emailType, email = recipient))
        }
    }
}
