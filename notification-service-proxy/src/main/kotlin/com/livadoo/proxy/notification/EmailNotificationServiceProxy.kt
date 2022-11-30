package com.livadoo.proxy.notification

import com.livadoo.proxy.notification.model.AccountCreateConfirmation
import com.livadoo.proxy.notification.model.CustomerAccount
import com.livadoo.proxy.notification.model.PasswordResetConfirmation
import com.livadoo.proxy.notification.model.StaffAccount
import com.livadoo.proxy.notification.model.PasswordResetRequest

interface NotificationServiceProxy {

    suspend fun sendStaffAccountCreateConfirmationEmail(account: StaffAccount): String

    suspend fun sendCustomerAccountVerificationEmail(account: CustomerAccount): String

    suspend fun sendCustomerAccountCreateConfirmationEmail(account: AccountCreateConfirmation): String

    suspend fun sendUserPasswordResetConfirmationEmail(resetConfirmation: PasswordResetConfirmation): String

    suspend fun sendUserPasswordResetRequestEmail(resetRequest: PasswordResetRequest): String
}