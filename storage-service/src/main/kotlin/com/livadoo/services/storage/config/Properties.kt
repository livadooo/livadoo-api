package com.livadoo.services.storage.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "livadoo")
data class LivadooProperties(
    val storage: Storage
) {
    data class Storage(
        val googleCloud: GoogleCloud,
        val local: LocalStorage,
    ) {
        data class GoogleCloud(
            val publicBucket: String,
            val oldBucket: String,
            val newBucket: String,
            val patchBucket: Boolean = false
        )

        data class LocalStorage(
            val path: String,
            val baseUrl: String,
            val oldBucket: String,
            val newBucket: String,
            val patchBucket: Boolean = false
        )
    }
}
