package com.livadoo.services.notification

import com.livadoo.services.notification.email.MailService
import com.livadoo.services.notification.twilio.TwilioMessagingService
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultNotificationService(
    private val twilioMessagingService: TwilioMessagingService,
    private val mailService: MailService,
) : NotificationService {
    override fun sendCustomerOtp(phoneNumber: String, otp: String, locale: Locale) {
        twilioMessagingService.sendOtp(phoneNumber = phoneNumber, otp = otp, locale = locale)
    }

    override fun notifyStaffAccountCreated(firstName: String, email: String, password: String, locale: Locale) {
        mailService.sendStaffAccountCreatedEmail(
            firstName = firstName,
            email = email,
            password = password,
            locale = locale,
        )
    }

    override fun notifyPasswordResetRequest(
        firstName: String,
        email: String,
        otp: String,
        locale: Locale,
    ) {
        mailService.sendPasswordResetRequestEmail(
            firstName = firstName,
            email = email,
            otp = otp,
            locale = locale,
        )
    }

    override fun notifyPasswordReset(firstName: String, email: String, locale: Locale) {
        mailService.sendPasswordResetEmail(firstName = firstName, email = email, locale = locale)
    }

    override fun notifyEmailChangeRequest(
        firstName: String,
        email: String,
        otp: String,
        locale: Locale,
    ) {
        mailService.sendEmailChangeRequestEmail(
            firstName = firstName,
            email = email,
            otp = otp,
            locale = locale,
        )
    }

    override fun notifyEmailChanged(firstName: String, email: String, locale: Locale) {
        mailService.sendEmailChangedEmail(firstName = firstName, email = email, locale = locale)
    }
}
