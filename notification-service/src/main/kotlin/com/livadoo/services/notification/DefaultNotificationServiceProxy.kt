package com.livadoo.services.notification

import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.ProxyLanguage
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class DefaultNotificationServiceProxy(
    private val notificationService: NotificationService,
) : NotificationServiceProxy {
    override fun sendCustomerOtp(phoneNumber: String, otp: String, language: ProxyLanguage) {
        notificationService.sendCustomerOtp(
            phoneNumber = phoneNumber,
            otp = otp,
            locale = Locale.forLanguageTag(language.name),
        )
    }

    override fun notifyStaffAccountCreated(
        firstName: String,
        email: String,
        password: String,
        language: ProxyLanguage,
    ) {
        notificationService.notifyStaffAccountCreated(
            firstName = firstName,
            email = email,
            password = password,
            locale = Locale.forLanguageTag(language.name),
        )
    }

    override fun notifyPasswordResetRequest(firstName: String, email: String, otp: String, language: ProxyLanguage) {
        notificationService.notifyPasswordResetRequest(
            firstName = firstName,
            email = email,
            otp = otp,
            locale = Locale.forLanguageTag(language.name),
        )
    }

    override fun notifyPasswordReset(firstName: String, email: String, language: ProxyLanguage) {
        notificationService.notifyPasswordReset(
            firstName = firstName,
            email = email,
            locale = Locale.forLanguageTag(language.name),
        )
    }

    override fun notifyEmailChangeRequest(firstName: String, email: String, otp: String, language: ProxyLanguage) {
        notificationService.notifyEmailChangeRequest(
            firstName = firstName,
            email = email,
            otp = otp,
            locale = Locale.forLanguageTag(language.name),
        )
    }

    override fun notifyEmailChanged(firstName: String, email: String, language: ProxyLanguage) {
        notificationService.notifyEmailChanged(
            firstName = firstName,
            email = email,
            locale = Locale.forLanguageTag(language.name),
        )
    }
}
