package com.livadoo.proxy.otp

interface OtpServiceProxy {
    suspend fun createOtp(subject: String, otpType: ProxyOtpType): String

    suspend fun isOtpValid(
        subject: String,
        password: String,
        otpType: ProxyOtpType,
    ): Boolean
}
