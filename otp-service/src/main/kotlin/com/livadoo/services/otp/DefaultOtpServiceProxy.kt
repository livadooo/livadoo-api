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
        proxyOtpType: ProxyOtpType,
    ): String {
        return otpService.createOtp(
            subject = subject,
            otpType = proxyOtpType.toOtpType(),
        )
    }

    override suspend fun isOtpValid(
        subject: String,
        password: String,
        proxyOtpType: ProxyOtpType,
    ): Boolean {
        return otpService.isOtpValid(
            subject = subject,
            password = password,
            otpType = proxyOtpType.toOtpType(),
        )
    }

    private fun ProxyOtpType.toOtpType(): OtpType = OtpType.valueOf(name)
}
