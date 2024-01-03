package com.livadoo.services.notification.email

interface TemplateService {

    fun getStaffAccountCreatedEmailContent(firstName: String, email: String, password: String): String

    fun getPasswordResetRequestContent(firstName: String, email: String, otp: String): String

    fun getPasswordResetContent(firstName: String, email: String): String

    fun getEmailChangeRequestContent(firstName: String, email: String, otp: String): String

    fun getEmailChangedContent(firstName: String, email: String): String
}
