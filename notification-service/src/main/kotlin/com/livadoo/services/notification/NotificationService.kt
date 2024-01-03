package com.livadoo.services.notification

import java.util.Locale

interface NotificationService {
    fun sendCustomerOtp(phoneNumber: String, otp: String, locale: Locale)

    fun notifyStaffAccountCreated(firstName: String, email: String, password: String, locale: Locale)

    fun notifyPasswordResetRequest(firstName: String, email: String, otp: String, locale: Locale)

    fun notifyPasswordReset(firstName: String, email: String, locale: Locale)

    fun notifyEmailChangeRequest(firstName: String, email: String, otp: String, locale: Locale)

    fun notifyEmailChanged(firstName: String, email: String, locale: Locale)
}
