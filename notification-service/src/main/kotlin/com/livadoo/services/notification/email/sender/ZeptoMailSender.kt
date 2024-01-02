package com.livadoo.services.notification.email.sender

import com.fasterxml.jackson.annotation.JsonProperty
import com.livadoo.services.notification.EmailType
import com.livadoo.services.notification.MailUtils.getMailErrorDescription
import com.livadoo.services.notification.MailUtils.getMailSendingDescription
import com.livadoo.services.notification.MailUtils.getMailSuccessDescription
import com.livadoo.services.notification.MailUtils.getSender
import com.livadoo.services.notification.Sender
import com.livadoo.services.notification.config.NotificationProperties
import com.livadoo.services.notification.email.EmailSender
import com.livadoo.utils.spring.awaitAndParseResponse
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service("zeptoMailSender")
class ZeptoMailSender(
    @Qualifier("zeptoMailWebClient") private val zeptoMailWebClient: WebClient,
    private val notificationProperties: NotificationProperties,
) : EmailSender {
    private val logger = LoggerFactory.getLogger(ZeptoMailSender::class.java)

    override fun send(recipient: String, subject: String, body: String, emailType: EmailType) {
        mono {
            try {
                val emailBody = EmailBody(
                    bounceAddress = notificationProperties.mail.zeptomail.bounceAddress,
                    sender = getSender(emailType),
                    receiver = listOf(EmailBody.Receiver(emailAddress = EmailBody.EmailAddress(address = recipient))),
                    subject = subject,
                    htmlBody = body,
                )
                logger.info(getMailSendingDescription(emailType = emailType, email = recipient))

                zeptoMailWebClient.post()
                    .uri("/email")
                    .bodyValue(emailBody)
                    .awaitAndParseResponse<Unit>()

                logger.info(getMailSuccessDescription(emailType = emailType, email = recipient))
            } catch (exception: Exception) {
                logger.error(getMailErrorDescription(emailType = emailType, email = recipient))
            }
        }.subscribe()
    }

    data class EmailBody(
        @field:JsonProperty("bounce_address")
        val bounceAddress: String,
        @field:JsonProperty("from")
        val sender: Sender,
        @field:JsonProperty("to")
        val receiver: List<Receiver>,
        val subject: String,
        val htmlBody: String,
    ) {
        data class Receiver(
            @field:JsonProperty("email_address") val emailAddress: EmailAddress,
        )

        data class EmailAddress(val address: String)
    }
}
