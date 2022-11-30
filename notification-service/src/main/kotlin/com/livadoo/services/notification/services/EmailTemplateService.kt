package com.livadoo.services.notification.services

import com.livadoo.services.notification.data.AccountCreateConfirmation
import com.livadoo.services.notification.data.CustomerAccount
import com.livadoo.services.notification.data.PasswordResetRequest
import com.livadoo.services.notification.data.PasswordResetConfirmation
import com.livadoo.services.notification.data.StaffAccount

interface EmailTemplateService {

    fun getStaffAccountCreateConfirmationContent(staffAccount: StaffAccount) : String

    fun getCustomerAccountVerificationContent(customerAccount: CustomerAccount) : String

    fun getCustomerAccountCreateConfirmationContent(accountCreate: AccountCreateConfirmation) : String

    fun getPasswordResetRequestContent(passwordResetRequest: PasswordResetRequest) : String

    fun getPasswordResetConfirmationContent(resetConfirmation: PasswordResetConfirmation) : String
}
