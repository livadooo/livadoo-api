package com.livadoo.services.notification.proxy

import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.model.AccountCreateConfirmation
import com.livadoo.proxy.notification.model.CustomerAccount
import com.livadoo.proxy.notification.model.PasswordResetRequest
import com.livadoo.proxy.notification.model.PasswordResetConfirmation
import com.livadoo.proxy.notification.model.StaffAccount
import com.livadoo.services.notification.services.MailService
import org.springframework.beans.factory.annotation.Autowired
import com.livadoo.services.notification.data.StaffAccount as InternalStaffAccount
import com.livadoo.services.notification.data.CustomerAccount as InternalCustomerAccount
import com.livadoo.services.notification.data.AccountCreateConfirmation as InternalAccountCreateConfirmation
import com.livadoo.services.notification.data.PasswordResetConfirmation as InternalPasswordResetConfirmation
import com.livadoo.services.notification.data.PasswordResetRequest as InternalPasswordResetRequest
import org.springframework.stereotype.Service

@Service
class NotificationServiceProxyImpl @Autowired constructor(
    private val mailService: MailService
) : NotificationServiceProxy {

    override suspend fun sendStaffAccountCreateConfirmationEmail(account: StaffAccount): String {
        return mailService.sendStaffAccountCreateConfirmation(account.toInternalStaffAccount())
    }

    override suspend fun sendCustomerAccountVerificationEmail(account: CustomerAccount): String {
        return mailService.sendCustomerAccountVerification(account.toInternalCustomerAccount())
    }

    override suspend fun sendCustomerAccountCreateConfirmationEmail(account: AccountCreateConfirmation): String {
        return mailService.sendCustomerAccountCreateConfirmation(account.toInternalAccountCreateConfirmation())
    }

    override suspend fun sendUserPasswordResetConfirmationEmail(resetConfirmation: PasswordResetConfirmation): String {
        return mailService.sendUserPasswordResetConfirmation(resetConfirmation.toInternalPasswordResetConfirmation())
    }

    override suspend fun sendUserPasswordResetRequestEmail(resetRequest: PasswordResetRequest): String {
        return mailService.sendUserPasswordResetRequest(resetRequest.toInternalPasswordResetRequest())
    }

    fun StaffAccount.toInternalStaffAccount() = InternalStaffAccount(
        userId, email, password, firstName
    )

    fun CustomerAccount.toInternalCustomerAccount() = InternalCustomerAccount(
        userId, email, verificationKey
    )

    fun AccountCreateConfirmation.toInternalAccountCreateConfirmation() = InternalAccountCreateConfirmation(
        userId, email, lastName
    )

    fun PasswordResetConfirmation.toInternalPasswordResetConfirmation() = InternalPasswordResetConfirmation(
        userId, email
    )

    fun PasswordResetConfirmation.toInternalPasswordResetRequest() = InternalPasswordResetConfirmation(
        userId, email
    )

    fun PasswordResetRequest.toInternalPasswordResetRequest() = InternalPasswordResetRequest(
        userId, email, resetKey, expirationTime
    )
}