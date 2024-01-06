package com.livadoo.services.otp

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "one-time-passwords")
data class OtpEntity(
    var otp: String,
    val subject: String,
    val type: OtpType,
    var expiresAt: Instant,
    @Id
    val id: String? = null,
    @Version
    val version: Int = 0,
)
