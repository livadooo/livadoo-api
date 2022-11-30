package com.livadoo.services.storage.data

import java.time.Instant

data class Document(
    var uuid: String,
    var uri: String,
    var name: String,
    var contentType: String,
    var createdAt: Instant,
    var id: String? = null
) {
    var content: ByteArray? = null
}
