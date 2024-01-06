package com.livadoo.services.otp

interface OtpService {
    suspend fun createOtp(subject: String, otpType: OtpType): String

    suspend fun validateOtp(otp: String, otpType: OtpType): String?

    suspend fun isOtpValid(otp: String, otpType: OtpType): Boolean
}
