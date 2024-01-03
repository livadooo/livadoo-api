package com.livadoo.services.otp

interface OtpService {
    suspend fun createOtp(subject: String, otpType: OtpType): String

    suspend fun isOtpValid(subject: String, password: String, otpType: OtpType): Boolean
}
