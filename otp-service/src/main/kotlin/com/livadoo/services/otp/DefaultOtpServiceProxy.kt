package com.livadoo.services.otp

import com.livadoo.proxy.otp.OtpServiceProxy
import com.livadoo.proxy.otp.ProxyOtpType
import org.springframework.stereotype.Service

@Service
class DefaultOtpServiceProxy(
    private val otpService: OtpService,
) : OtpServiceProxy {
    override suspend fun createOtp(subject: String, otpType: ProxyOtpType): String {
        return otpService.createOtp(subject = subject, otpType = otpType.toOtpType())
    }

    override suspend fun validateOtp(otp: String, otpType: ProxyOtpType): String? {
        return otpService.validateOtp(otp = otp, otpType = otpType.toOtpType())
    }

    override suspend fun isOtpValid(otp: String, otpType: ProxyOtpType): Boolean {
        return otpService.isOtpValid(otp = otp, otpType = otpType.toOtpType())
    }

    private fun ProxyOtpType.toOtpType(): OtpType = OtpType.valueOf(name)
}
