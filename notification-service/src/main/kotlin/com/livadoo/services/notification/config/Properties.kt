package com.livadoo.services.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "notification.zeptomail")
data class ZeptoMailProperties(
    val apiUrl: String,
    val authorizationHeader: String,
    val bounceAddress: String
)