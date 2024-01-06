package com.livadoo.services.otp

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.Instant

interface OtpRepository : CoroutineCrudRepository<OtpEntity, String> {
    suspend fun findByOtpAndTypeAndExpiresAtGreaterThanEqual(otp: String, type: OtpType, expiresAt: Instant): OtpEntity?

    suspend fun findBySubjectAndTypeAndExpiresAtGreaterThan(
        subject: String,
        type: OtpType,
        expiresAt: Instant,
    ): OtpEntity?

    suspend fun existsByOtpAndTypeAndExpiresAtGreaterThanEqual(otp: String, type: OtpType, expiresAt: Instant): Boolean
}
