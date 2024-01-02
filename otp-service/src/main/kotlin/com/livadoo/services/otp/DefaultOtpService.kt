package com.livadoo.services.otp

import org.springframework.stereotype.Service
import java.time.Clock
import java.time.temporal.ChronoUnit
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class DefaultOtpService(
    private val otpRepository: OtpRepository,
    private val clock: Clock,
    private val otpProperties: OtpProperties,
) : OtpService {
    override suspend fun createOtp(
        subject: String,
        otpType: OtpType,
    ): String {
        var otpEntity =
            otpRepository.findBySubjectAndTypeAndExpiresAtGreaterThan(
                subject = subject,
                type = otpType,
                expiresAt = clock.instant(),
            )
        val expirationTime = getExpirationTime()
        if (otpEntity != null) {
            otpEntity.apply {
                expiresAt = clock.instant().plus(expirationTime, ChronoUnit.MINUTES)
                password = randomPassword
            }
        } else {
            otpEntity =
                OtpEntity(
                    password = randomPassword,
                    subject = subject,
                    type = otpType,
                    expiresAt = clock.instant().plus(expirationTime, ChronoUnit.MINUTES),
                )
        }
        return otpRepository.save(otpEntity).password
    }

    override suspend fun isOtpValid(
        subject: String,
        password: String,
        otpType: OtpType,
    ): Boolean {
        val otpEntity =
            otpRepository.findBySubjectAndPasswordAndTypeAndExpiresAtGreaterThanEqual(
                subject = subject,
                password = password,
                type = otpType,
                expiresAt = clock.instant(),
            )
        if (otpEntity != null) {
            otpRepository.deleteBySubjectAndPasswordAndType(
                subject = subject,
                password = password,
                type = otpType,
            )
            return true
        }
        return false
    }

    private fun getExpirationTime(): Long {
        return otpProperties.customerAuth.expirationTimeInMinutes
    }

    private val randomPassword: String
        get() = Random.nextInt(IntRange(100000, 999999)).toString()
}
