package com.livadoo.services.notification.services.email

import com.fasterxml.jackson.annotation.JsonProperty
import com.livadoo.services.notification.config.ZeptoMailProperties
import com.livadoo.services.notification.data.DeliveryStatus
import com.livadoo.services.notification.services.EmailSender
import com.livadoo.utils.spring.awaitAndParseResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service("zeptoMailSender")
class ZeptoMailSender(
    @Qualifier("zeptoMailWebClient") private val zeptoMailWebClient: WebClient,
    private val zeptoMailProperties: ZeptoMailProperties,
) : EmailSender {
    private val logger: Logger = LoggerFactory.getLogger(ZeptoMailSender::class.java)

    override suspend fun send(
        userId: String,
        email: String,
        subject: String,
        body: String,
    ): DeliveryStatus {
        return try {
            logger.info("Sending email for user: $userId")
            val emailBody =
                EmailBody(
                    bounceAddress = zeptoMailProperties.bounceAddress,
                    sender = EmailBody.Sender(address = "account-update@livadoo.com", name = "Livadoo"),
                    receiver = listOf(EmailBody.Receiver(emailAddress = EmailBody.EmailAddress(address = email))),
                    subject = subject,
                    htmlBody = body,
                )
            zeptoMailWebClient.post()
                .uri("/email")
                .bodyValue(emailBody)
                .awaitAndParseResponse<Unit>()

            logger.info("Email sent for user: $userId")
            DeliveryStatus.SENT
        } catch (ex: Exception) {
            logger.error("Fail to send email for user: $userId", ex)
            DeliveryStatus.FAILED
        }
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
        data class Sender(val address: String, val name: String)

        data class Receiver(
            @field:JsonProperty("email_address") val emailAddress: EmailAddress,
        )

        data class EmailAddress(val address: String)
    }
}
