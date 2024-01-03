package com.livadoo.services.notification.email.template

import com.livadoo.services.notification.email.TemplateService
import org.springframework.stereotype.Service
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.context.Context

@Service
class ResourcesTemplateService(
    private val templateEngine: ITemplateEngine,
) : TemplateService {

    override fun getStaffAccountCreatedEmailContent(firstName: String, email: String, password: String): String {
        return with(Context()) {
            setVariables(
                mapOf(
                    "email" to email,
                    "password" to password,
                    "firstName" to firstName,
                ),
            )
            templateEngine.process("staff-account-created", this)
        }
    }

    override fun getPasswordResetRequestContent(firstName: String, email: String, otp: String): String {
        return with(Context()) {
            setVariable("otp", otp)
            templateEngine.process("password-reset-request", this)
        }
    }

    override fun getPasswordResetContent(firstName: String, email: String): String {
        return with(Context()) {
            setVariable("email", email)
            setVariable("firstName", firstName)
            templateEngine.process("password-reset", this)
        }
    }

    override fun getEmailChangeRequestContent(firstName: String, email: String, otp: String): String {
        return with(Context()) {
            setVariable("otp", otp)
            templateEngine.process("email-change-request", this)
        }
    }

    override fun getEmailChangedContent(firstName: String, email: String): String {
        return with(Context()) {
            setVariable("email", email)
            setVariable("firstName", firstName)
            templateEngine.process("email-changed", this)
        }
    }
}
