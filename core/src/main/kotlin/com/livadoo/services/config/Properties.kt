package com.livadoo.services.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.data.mongodb")
data class MongodbProperties(
    val database: String,
    val uri: String
)