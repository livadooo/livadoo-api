package com.livadoo.services.otp

import com.livadoo.proxy.otp.OtpServiceProxy
import com.livadoo.proxy.otp.ProxyOtpType
import org.springframework.stereotype.Service

@Service
class DefaultOtpServiceProxy(
    private val otpService: OtpService,
) : OtpServiceProxy {
    override suspend fun createOtp(
        subject: String,
        otpType: ProxyOtpType,
    ): String {
        return otpService.createOtp(
            subject = subject,
            otpType = otpType.toOtpType(),
        )
    }

    override suspend fun isOtpValid(
        subject: String,
        password: String,
        otpType: ProxyOtpType,
    ): Boolean {
        return otpService.isOtpValid(
            subject = subject,
            password = password,
            otpType = otpType.toOtpType(),
        )
    }

    private fun ProxyOtpType.toOtpType(): OtpType = OtpType.valueOf(name)
}
