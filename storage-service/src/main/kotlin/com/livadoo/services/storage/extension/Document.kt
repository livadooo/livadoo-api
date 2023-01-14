package com.livadoo.services.storage.extension

import com.livadoo.services.storage.data.Document
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

/**
 * Extracts data from Document instance and builds a HttpHeader response
 */

fun Document.extractHeaders(): HttpHeaders {
    return HttpHeaders().apply {
        this.contentType = MediaType.parseMediaType(this@extractHeaders.contentType)
        contentDisposition = ContentDisposition.builder("attachment").filename(name).build()
    }
}