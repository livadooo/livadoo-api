package com.livadoo.common.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URI

@ResponseStatus(HttpStatus.FORBIDDEN)
class NotAllowedException(
    override val title: String = "Forbidden",
    override val detail: String = "You are not allowed to perform this operation",
    override val statusCode: String = "40300",
    override val type: URI = DEFAULT_TYPE,
    override val status: HttpStatus = HttpStatus.FORBIDDEN,
) : AbstractThrowableProblem(title, detail)