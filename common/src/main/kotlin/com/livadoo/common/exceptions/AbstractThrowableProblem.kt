package com.livadoo.common.exceptions

import org.springframework.http.HttpStatus
import java.net.URI

abstract class AbstractThrowableProblem(
    open val title: String,
    open val detail: String
) : RuntimeException("$title: $detail") {
    abstract val statusCode: String
    abstract val type: URI
    abstract val status: HttpStatus
}

val DEFAULT_TYPE: URI = URI.create("about:blank")