package com.livadoo.proxy.otp

interface OtpServiceProxy {
    suspend fun createOtp(
        subject: String,
        proxyOtpType: ProxyOtpType,
    ): String

    suspend fun isOtpValid(
        subject: String,
        password: String,
        proxyOtpType: ProxyOtpType,
    ): Boolean
}
