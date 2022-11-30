package com.livadoo.services.notification.services.email

import com.livadoo.services.notification.data.DeliveryStatus
import com.livadoo.services.notification.services.EmailSender
import jakarta.mail.internet.MimeMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class SmtpEmailSender constructor(
    private val javaMailSender: JavaMailSender
) : EmailSender {

    private val logger: Logger = LoggerFactory.getLogger(SmtpEmailSender::class.java)

    override suspend fun send(userId: String, email: String, subject: String, body: String): DeliveryStatus {
        val message: MimeMessage = javaMailSender.createMimeMessage()

        MimeMessageHelper(
            message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        ).apply {
            setTo(email)
            setSubject(subject)
            setText(body, true)
            setFrom("account-update@livadoo.com", "account-update@livadoo.com")
        }

        return try {
            logger.info("Sending email for user: $userId")
            javaMailSender.send(message)
            logger.info("Email sent for user: $userId")
            DeliveryStatus.SENT
        } catch (ex: Exception) {
            logger.error("Fail to send email for user: $userId", ex)
            DeliveryStatus.FAILED
        }
    }
}