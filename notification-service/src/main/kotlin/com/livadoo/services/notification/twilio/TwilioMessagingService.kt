package com.livadoo.services.notification.twilio

import java.util.Locale

interface TwilioMessagingService {
    fun sendOtp(phoneNumber: String, otp: String, locale: Locale)
}
