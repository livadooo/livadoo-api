package com.livadoo.services.notification.config

import com.livadoo.services.notification.MailProvider
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo.notification")
data class NotificationProperties(
    val mail: Mail,
    val twilio: Twilio,
) {
    data class Mail(
        val activeProvider: MailProvider,
        val zeptomail: Zeptomail,
        val sendgrid: Sendgrid,
    )

    data class Zeptomail(
        val apiUrl: String,
        val authorizationHeader: String,
        val bounceAddress: String,
    )

    data class Sendgrid(val apiKey: String)

    data class Twilio(val account: TwilioAccount, val messaging: TwilioMessaging)

    data class TwilioAccount(val sid: String, val token: String)

    data class TwilioMessaging(
        val contentSidEn: String,
        val contentSidFr: String,
        val messagingServiceId: String,
    )
}
