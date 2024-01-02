package com.livadoo.services.notification

object MailUtils {
    fun getSender(emailType: EmailType): Sender {
        return when (emailType) {
            EmailType.STAFF_ACCOUNT_CREATED,
            EmailType.PASSWORD_RESET_REQUEST,
            EmailType.PASSWORD_RESET,
            EmailType.EMAIL_CHANGE_REQUEST,
            EmailType.EMAIL_CHANGED,
            -> Sender(address = "no-reply@livadoo.com", name = "Livadoo")
        }
    }

    fun getMailSendingDescription(emailType: EmailType, email: String): String {
        return when (emailType) {
            EmailType.STAFF_ACCOUNT_CREATED -> "Sending staff account creation confirmation email to $email"
            EmailType.PASSWORD_RESET_REQUEST -> "Sending password reset request email to $email"
            EmailType.PASSWORD_RESET -> "Sending password reset confirmation email to $email"
            EmailType.EMAIL_CHANGE_REQUEST -> "Sending email change request email to $email"
            EmailType.EMAIL_CHANGED -> "Sending email change confirmation email to $email"
        }
    }

    fun getMailSuccessDescription(emailType: EmailType, email: String): String {
        return when (emailType) {
            EmailType.STAFF_ACCOUNT_CREATED -> "Staff account creation confirmation email was successfully sent to $email"
            EmailType.PASSWORD_RESET_REQUEST -> "Password reset request email was successfully sent to $email"
            EmailType.PASSWORD_RESET -> "Password reset confirmation email was successfully sent to $email"
            EmailType.EMAIL_CHANGE_REQUEST -> "Email change request email was successfully sent to $email"
            EmailType.EMAIL_CHANGED -> "Email change confirmation email was successfully sent to $email"
        }
    }

    fun getMailErrorDescription(emailType: EmailType, email: String): String {
        return when (emailType) {
            EmailType.STAFF_ACCOUNT_CREATED -> "Failed to send staff account creation confirmation email to $email"
            EmailType.PASSWORD_RESET_REQUEST -> "Failed to send password reset request email to $email"
            EmailType.PASSWORD_RESET -> "Failed to send password reset confirmation email to $email"
            EmailType.EMAIL_CHANGE_REQUEST -> "Failed to send email change request email to $email"
            EmailType.EMAIL_CHANGED -> "Failed to send email change confirmation email to $email"
        }
    }
}
