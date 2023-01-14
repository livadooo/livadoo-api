package com.livadoo.services.common.utils

import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.http.codec.multipart.FilePart
import java.io.ByteArrayOutputStream

/**
 * Extracts contentType and the bytes representation of the file.
 */
suspend fun FilePart.extractContent(): Pair<String, ByteArray> {
    val contentType = this.headers()["content-type"]!![0]
    val bytesOutputStream = ByteArrayOutputStream()

    this.content()
        .map { it.toByteBuffer().array() }
        .collectList()
        .awaitFirst()
        .forEach { bytes -> bytesOutputStream.write(bytes) }

    return Pair(contentType, bytesOutputStream.toByteArray())
}