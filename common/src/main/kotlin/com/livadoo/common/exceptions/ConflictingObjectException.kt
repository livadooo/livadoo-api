package com.livadoo.common.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URI

@ResponseStatus(HttpStatus.NOT_FOUND)
abstract class ConflictingObjectException(
    override val title: String,
    override val detail: String,
    override val statusCode: String,
    override val type: URI = DEFAULT_TYPE,
    override val status: HttpStatus = HttpStatus.CONFLICT,
) : AbstractThrowableProblem(title, detail)