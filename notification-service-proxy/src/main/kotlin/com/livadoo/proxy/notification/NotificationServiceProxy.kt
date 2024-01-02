package com.livadoo.proxy.notification

interface NotificationServiceProxy {
    fun sendCustomerOtp(
        phoneNumber: String,
        otp: String,
        language: ProxyLanguage,
    )

    fun notifyStaffAccountCreated(
        firstName: String,
        email: String,
        password: String,
        language: ProxyLanguage,
    )

    fun notifyPasswordResetRequest(
        firstName: String,
        email: String,
        otp: String,
        expirationTime: Int,
        language: ProxyLanguage,
    )

    fun notifyPasswordReset(
        firstName: String,
        email: String,
        language: ProxyLanguage,
    )

    fun notifyEmailChangeRequest(
        firstName: String,
        email: String,
        otp: String,
        expirationTime: Int,
        language: ProxyLanguage,
    )

    fun notifyEmailChanged(
        firstName: String,
        email: String,
        language: ProxyLanguage,
    )
}
