package com.livadoo.services.notification.email

import java.util.Locale

interface MailService {
    fun sendStaffAccountCreatedEmail(firstName: String, email: String, password: String, locale: Locale)

    fun sendPasswordResetRequestEmail(firstName: String, email: String, otp: String, locale: Locale)

    fun sendPasswordResetEmail(firstName: String, email: String, locale: Locale)

    fun sendEmailChangeRequestEmail(firstName: String, email: String, otp: String, locale: Locale)

    fun sendEmailChangedEmail(firstName: String, email: String, locale: Locale)
}
