package com.livadoo.services.storage.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo")
data class LivadooProperties(
    val storage: Storage
) {
    data class Storage(
        val path: String,
        val googleCloud: GoogleCloud
    ) {
        data class GoogleCloud(
            val bucket: String,
            val oldBucket: String,
            val patchBucket: Boolean = false
        )
    }
}
