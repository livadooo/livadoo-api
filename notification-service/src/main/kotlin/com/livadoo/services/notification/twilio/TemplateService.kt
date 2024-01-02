package com.livadoo.services.notification.twilio

import java.util.Locale

interface TemplateService {
    fun getContentSidId(locale: Locale): String
}
