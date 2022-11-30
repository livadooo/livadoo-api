package com.livadoo.services.notification.services

import com.livadoo.services.notification.data.AccountCreateConfirmation
import com.livadoo.services.notification.data.CustomerAccount
import com.livadoo.services.notification.data.PasswordResetRequest
import com.livadoo.services.notification.data.PasswordResetConfirmation
import com.livadoo.services.notification.data.StaffAccount


interface MailService {

    suspend fun sendStaffAccountCreateConfirmation(account: StaffAccount): String

    suspend fun sendCustomerAccountVerification(account: CustomerAccount): String

    suspend fun sendCustomerAccountCreateConfirmation(account: AccountCreateConfirmation): String

    suspend fun sendUserPasswordResetConfirmation(passwordReset: PasswordResetConfirmation): String

    suspend fun sendUserPasswordResetRequest(resetRequest: PasswordResetRequest): String
}
