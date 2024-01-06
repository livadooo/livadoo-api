package com.livadoo.services.otp

import com.livadoo.services.otp.OtpType.CUSTOMER_AUTH
import com.livadoo.services.otp.OtpType.STAFF_PASSWORD_RESET
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Clock
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class DefaultOtpService(
    private val otpRepository: OtpRepository,
    private val clock: Clock,
    private val otpProperties: OtpProperties,
) : OtpService {
    override suspend fun createOtp(subject: String, otpType: OtpType): String {
        var otpEntity = otpRepository.findBySubjectAndTypeAndExpiresAtGreaterThan(
            subject = subject,
            type = otpType,
            expiresAt = clock.instant(),
        )
        val expirationTime = getExpirationTime(otpType)
        if (otpEntity != null) {
            otpEntity.apply {
                expiresAt = clock.instant().plus(expirationTime.toLong(), ChronoUnit.MINUTES)
                otp = buildOtp(otpType)
            }
        } else {
            otpEntity = OtpEntity(
                otp = buildOtp(otpType),
                subject = subject,
                type = otpType,
                expiresAt = clock.instant().plus(expirationTime.toLong(), ChronoUnit.MINUTES),
            )
        }
        otpEntity = otpRepository.save(otpEntity)

        return when (otpType) {
            CUSTOMER_AUTH -> otpEntity.otp
            STAFF_PASSWORD_RESET -> buildResetLink(otpEntity.otp)
        }
    }

    override suspend fun validateOtp(otp: String, otpType: OtpType): String? {
        return otpRepository.findByOtpAndTypeAndExpiresAtGreaterThanEqual(
            otp = otp,
            type = otpType,
            expiresAt = clock.instant(),
        )?.let {
            otpRepository.delete(it)
            it.subject
        }
    }

    override suspend fun isOtpValid(otp: String, otpType: OtpType): Boolean {
        return otpRepository.existsByOtpAndTypeAndExpiresAtGreaterThanEqual(
            otp = otp,
            type = otpType,
            expiresAt = clock.instant(),
        )
    }

    private fun getExpirationTime(otpType: OtpType): Int {
        return when (otpType) {
            CUSTOMER_AUTH -> otpProperties.customerAuth.validityTimeInMinutes
            STAFF_PASSWORD_RESET -> otpProperties.staffPasswordReset.validityTimeInHours
        }
    }

    private fun buildOtp(otpType: OtpType): String {
        return when (otpType) {
            CUSTOMER_AUTH -> Random.nextInt(IntRange(100000, 999999)).toString()
            STAFF_PASSWORD_RESET -> {
                val secureRandom = SecureRandom()
                val randomBytes = ByteArray(64)
                secureRandom.nextBytes(randomBytes)
                Base64.getUrlEncoder().encodeToString(randomBytes)
            }
        }
    }

    private fun buildResetLink(otp: String): String {
        return "${otpProperties.staffPasswordReset.adminxUrl}/reset-password?token=$otp"
    }
}
