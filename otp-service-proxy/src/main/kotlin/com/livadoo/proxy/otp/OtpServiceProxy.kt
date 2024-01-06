package com.livadoo.proxy.otp

interface OtpServiceProxy {
    suspend fun createOtp(subject: String, otpType: ProxyOtpType): String

    suspend fun validateOtp(otp: String, otpType: ProxyOtpType): String?

    suspend fun isOtpValid(otp: String, otpType: ProxyOtpType): Boolean
}
