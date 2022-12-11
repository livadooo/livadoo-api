package com.livadoo.common.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URI

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(
    override val title: String = "You are unauthorized",
    override val detail: String = "You are not authenticated",
    override val statusCode: String = "40100",
    override val type: URI = DEFAULT_TYPE,
    override val status: HttpStatus = HttpStatus.UNAUTHORIZED,
) : AbstractThrowableProblem(title, detail)