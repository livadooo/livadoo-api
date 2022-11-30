package com.livadoo.services.notification.services.mongodb

import com.livadoo.services.notification.data.AccountCreateConfirmation
import com.livadoo.services.notification.data.CustomerAccount
import com.livadoo.services.notification.data.DeliveryStatus
import com.livadoo.services.notification.data.EmailType
import com.livadoo.services.notification.data.EmailType.STAFF_ACCOUNT_CREATE_CONFIRMATION
import com.livadoo.services.notification.data.EmailType.CUSTOMER_ACCOUNT_VERIFICATION
import com.livadoo.services.notification.data.EmailType.CUSTOMER_ACCOUNT_CREATE_CONFIRMATION
import com.livadoo.services.notification.data.EmailType.PASSWORD_RESET_REQUEST
import com.livadoo.services.notification.data.EmailType.PASSWORD_RESET_CONFIRMATION
import com.livadoo.services.notification.data.PasswordResetRequest
import com.livadoo.services.notification.data.PasswordResetConfirmation
import com.livadoo.services.notification.data.StaffAccount
import com.livadoo.services.notification.services.EmailSender
import com.livadoo.services.notification.services.EmailTemplateService
import com.livadoo.services.notification.services.MailService
import com.livadoo.services.notification.services.mongodb.entity.EmailEntity
import com.livadoo.services.notification.services.mongodb.repository.EmailRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MongoMailService @Autowired constructor(
    private val emailRepository: EmailRepository,
    private val emailSender: EmailSender,
    private val emailTemplateService: EmailTemplateService
) : MailService {

    private val logger = LoggerFactory.getLogger(MongoMailService::class.java)

    override suspend fun sendStaffAccountCreateConfirmation(account: StaffAccount): String {
        val emailType = STAFF_ACCOUNT_CREATE_CONFIRMATION
        val subject = getEmailSubject(emailType)
        val body = emailTemplateService.getStaffAccountCreateConfirmationContent(account)

        return sendAndSaveEmail(account.userId, account.email, subject, body, emailType)
    }

    override suspend fun sendCustomerAccountVerification(account: CustomerAccount): String {
        val emailType = CUSTOMER_ACCOUNT_VERIFICATION
        val subject = getEmailSubject(emailType)
        val body = emailTemplateService.getCustomerAccountVerificationContent(account)

        return sendAndSaveEmail(account.userId, account.email, subject, body, emailType)
    }

    override suspend fun sendCustomerAccountCreateConfirmation(account: AccountCreateConfirmation): String {
        val emailType = CUSTOMER_ACCOUNT_CREATE_CONFIRMATION
        val subject = getEmailSubject(emailType)
        val body = emailTemplateService.getCustomerAccountCreateConfirmationContent(account)

        return sendAndSaveEmail(account.userId, account.email, subject, body, emailType)
    }

    override suspend fun sendUserPasswordResetConfirmation(passwordReset: PasswordResetConfirmation): String {
        val emailType = PASSWORD_RESET_CONFIRMATION
        val subject = getEmailSubject(emailType)
        val body = emailTemplateService.getPasswordResetConfirmationContent(passwordReset)

        return sendAndSaveEmail(passwordReset.userId, passwordReset.email, subject, body, emailType)
    }

    override suspend fun sendUserPasswordResetRequest(resetRequest: PasswordResetRequest): String {
        val emailType = PASSWORD_RESET_REQUEST
        val subject = getEmailSubject(emailType)
        val body = emailTemplateService.getPasswordResetRequestContent(resetRequest)

        return sendAndSaveEmail(resetRequest.userId, resetRequest.email, subject, body, emailType)
    }

    private fun getEmailSubject(emailType: EmailType): String = when (emailType) {
        STAFF_ACCOUNT_CREATE_CONFIRMATION -> "Informations de connexion au compte"
        CUSTOMER_ACCOUNT_VERIFICATION -> "Vérifier votre compte Livadoo"
        CUSTOMER_ACCOUNT_CREATE_CONFIRMATION -> "Confirmation de création de compte"
        PASSWORD_RESET_REQUEST -> "Demande de réinitialisation du mot de passe"
        PASSWORD_RESET_CONFIRMATION -> "Confirmation de réinitialisation du mot de passe"
    }

    private suspend fun sendAndSaveEmail(userId: String, email: String, subject: String, body: String, emailType: EmailType): String {
        val deliveryStatus = emailSender.send(userId, email, subject, body)

        val emailEntity = EmailEntity(userId, deliveryStatus, emailType)

        val savedEntity = emailRepository.save(emailEntity).awaitSingle().also {
            logger.info("Saved email with id : ${it.id}")
        }

        return if (savedEntity.status == DeliveryStatus.SENT) {
            "Email de ${subject.lowercase(Locale.getDefault())} envoyé avec succès."
        } else {
            "Échec d'envoie du mail de ${subject.lowercase(Locale.getDefault())}"
        }
    }
}