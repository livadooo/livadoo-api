package com.livadoo.services.otp

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

interface OtpRepository : CoroutineCrudRepository<OtpEntity, String> {
    suspend fun findBySubjectAndPasswordAndTypeAndExpiresAtGreaterThanEqual(
        subject: String,
        password: String,
        type: OtpType,
        expiresAt: Instant,
    ): OtpEntity?

    suspend fun findBySubjectAndTypeAndExpiresAtGreaterThan(
        subject: String,
        type: OtpType,
        expiresAt: Instant,
    ): OtpEntity?

    @Transactional
    suspend fun deleteBySubjectAndPasswordAndType(
        subject: String,
        password: String,
        type: OtpType,
    )
}
