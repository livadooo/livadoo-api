package com.livadoo.services.notification.email

import com.livadoo.services.notification.EmailType
import com.livadoo.services.notification.EmailType.EMAIL_CHANGED
import com.livadoo.services.notification.EmailType.EMAIL_CHANGE_REQUEST
import com.livadoo.services.notification.EmailType.PASSWORD_RESET
import com.livadoo.services.notification.EmailType.PASSWORD_RESET_REQUEST
import com.livadoo.services.notification.EmailType.STAFF_ACCOUNT_CREATED
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class DefaultMailService(
    private val templateService: TemplateService,
    private val messageSource: MessageSource,
    emailSenderFactory: EmailSenderFactory,
) : MailService {
    private val emailSender = emailSenderFactory.getSender()

    override fun sendStaffAccountCreatedEmail(firstName: String, email: String, password: String, locale: Locale) {
        val emailType = STAFF_ACCOUNT_CREATED
        val subject = getSubject(emailType = emailType, locale = locale)
        val body = templateService.getStaffAccountCreatedEmailContent(
            firstName = firstName,
            email = email,
            password = password,
        )
        emailSender.send(recipient = email, subject = subject, body = body, emailType = emailType)
    }

    override fun sendPasswordResetRequestEmail(
        firstName: String,
        email: String,
        otp: String,
        expirationTime: Int,
        locale: Locale,
    ) {
        val emailType = PASSWORD_RESET_REQUEST
        val subject = getSubject(emailType = emailType, locale = locale)
        val body = templateService.getPasswordResetRequestContent(
            firstName = firstName,
            email = email,
            otp = otp,
            expirationTime = expirationTime,
        )
        emailSender.send(recipient = email, subject = subject, body = body, emailType = emailType)
    }

    override fun sendPasswordResetEmail(firstName: String, email: String, locale: Locale) {
        val emailType = PASSWORD_RESET
        val subject = getSubject(emailType = emailType, locale = locale)
        val body = templateService.getPasswordResetContent(
            firstName = firstName,
            email = email,
        )
        emailSender.send(recipient = email, subject = subject, body = body, emailType = emailType)
    }

    override fun sendEmailChangeRequestEmail(
        firstName: String,
        email: String,
        otp: String,
        expirationTime: Int,
        locale: Locale,
    ) {
        val emailType = EMAIL_CHANGE_REQUEST
        val subject = getSubject(emailType = emailType, locale = locale)
        val body = templateService.getEmailChangeRequestContent(
            firstName = firstName,
            email = email,
            otp = otp,
            expirationTime = expirationTime,
        )
        emailSender.send(recipient = email, subject = subject, body = body, emailType = emailType)
    }

    override fun sendEmailChangedEmail(firstName: String, email: String, locale: Locale) {
        val emailType = EMAIL_CHANGED
        val subject = getSubject(emailType = emailType, locale = locale)
        val body = templateService.getEmailChangedContent(
            email = email,
            firstName = firstName,
        )
        emailSender.send(recipient = email, subject = subject, body = body, emailType = emailType)
    }

    private fun getSubject(emailType: EmailType, locale: Locale): String {
        return messageSource.getMessage(emailType.name, null, locale)
    }
}
