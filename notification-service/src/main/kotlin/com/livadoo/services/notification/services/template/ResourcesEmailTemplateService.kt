package com.livadoo.services.notification.services.template

import com.livadoo.services.notification.data.AccountCreateConfirmation
import com.livadoo.services.notification.data.CustomerAccount
import com.livadoo.services.notification.data.PasswordResetRequest
import com.livadoo.services.notification.data.PasswordResetConfirmation
import com.livadoo.services.notification.data.StaffAccount
import com.livadoo.services.notification.services.EmailTemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.context.Context

@Service
class ResourcesEmailTemplateService @Autowired constructor(
    private val templateEngine: ITemplateEngine
) : EmailTemplateService {

    override fun getStaffAccountCreateConfirmationContent(staffAccount: StaffAccount): String {
        val (_, email, password, firstName) = staffAccount

        return with(Context()) {
            this.setVariables(
                mapOf(
                    "email" to email,
                    "password" to password,
                    "firstName" to firstName
                )
            )
            templateEngine.process("staff-account-create-confirmation", this)
        }
    }

    override fun getCustomerAccountVerificationContent(customerAccount: CustomerAccount): String {
        val (_, _, verificationKey) = customerAccount

        return with(Context()) {
            this.setVariable("verificationKey", verificationKey)
            templateEngine.process("customer-account-verification", this)
        }
    }

    override fun getCustomerAccountCreateConfirmationContent(accountCreate: AccountCreateConfirmation): String {
        val (_, email, lastName) = accountCreate

        return with(Context()) {
            this.setVariables(
                mapOf(
                    "email" to email,
                    "lastName" to lastName
                )
            )
            templateEngine.process("customer-account-create-confirmation", this)
        }
    }

    override fun getPasswordResetRequestContent(passwordResetRequest: PasswordResetRequest): String {
        val (_, _, resetKey, expirationTime) = passwordResetRequest

        return with(Context()) {
            this.setVariables(
                mapOf(
                    "resetKey" to resetKey,
                    "expirationTime" to expirationTime
                )
            )
            templateEngine.process("password-reset-request", this)
        }
    }

    override fun getPasswordResetConfirmationContent(resetConfirmation: PasswordResetConfirmation): String {
        val (_, email) = resetConfirmation

        return with(Context()) {
            this.setVariable("email", email)
            templateEngine.process("password-reset-confirmation", this)
        }
    }
}