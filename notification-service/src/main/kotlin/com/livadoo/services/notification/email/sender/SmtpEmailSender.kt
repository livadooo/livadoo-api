package com.livadoo.services.notification.email.sender

import com.livadoo.services.notification.EmailType
import com.livadoo.services.notification.MailUtils
import com.livadoo.services.notification.MailUtils.getMailErrorDescription
import com.livadoo.services.notification.MailUtils.getMailSendingDescription
import com.livadoo.services.notification.MailUtils.getMailSuccessDescription
import com.livadoo.services.notification.email.EmailSender
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets

@Service("smtpSender")
class SmtpEmailSender(
    private val javaMailSender: JavaMailSender,
) : EmailSender {
    private val logger = LoggerFactory.getLogger(SmtpEmailSender::class.java)

    override fun send(recipient: String, subject: String, body: String, emailType: EmailType) {
        val message = javaMailSender.createMimeMessage()
        val sender = MailUtils.getSender(emailType)

        MimeMessageHelper(
            message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name(),
        ).apply {
            setTo(recipient)
            setSubject(subject)
            setText(body, true)
            setFrom(sender.address, sender.name)
        }

        try {
            logger.info(getMailSendingDescription(emailType = emailType, email = recipient))
            javaMailSender.send(message)
            logger.info(getMailSuccessDescription(emailType = emailType, email = recipient))
        } catch (exception: IOException) {
            logger.error(getMailErrorDescription(emailType = emailType, email = recipient))
        }
    }
}
