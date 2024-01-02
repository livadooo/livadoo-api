package com.livadoo.services.notification.twilio

import com.livadoo.services.notification.config.NotificationProperties
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class TwilioTemplateService(
    private val notificationProperties: NotificationProperties,
) : TemplateService {
    override fun getContentSidId(locale: Locale): String {
        if (locale == Locale.FRENCH) {
            return notificationProperties.twilio.messaging.contentSidFr
        }
        return notificationProperties.twilio.messaging.contentSidEn
    }
}
