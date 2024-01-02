package com.livadoo.services.notification.twilio

import com.google.gson.Gson
import com.livadoo.services.notification.config.NotificationProperties
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Locale
import kotlin.collections.mapOf

@Service
class WhatsAppService(
    private val notificationProperties: NotificationProperties,
    private val templateService: TemplateService,
) : TwilioMessagingService {
    private val logger = LoggerFactory.getLogger(WhatsAppService::class.java)

    override fun sendOtp(phoneNumber: String, otp: String, locale: Locale) {
        val messagingServiceId = notificationProperties.twilio.messaging.messagingServiceId
        val contentSid = templateService.getContentSidId(locale)
        val contentVariables = Gson().toJson(mapOf("1" to otp))
        val to = String.format("whatsapp:%s", phoneNumber)

        Message.creator(PhoneNumber(to), messagingServiceId, "")
            .setContentVariables(contentVariables)
            .setContentSid(contentSid)
            .createAsync()
            .handle { _, throwable ->
                if (throwable != null) {
                    logger.error("Failed to send verification code to phone number: $phoneNumber with details --> ${throwable.message}")
                } else {
                    logger.info("Successfully sent verification code to phone number: $phoneNumber")
                }
            }
    }
}
