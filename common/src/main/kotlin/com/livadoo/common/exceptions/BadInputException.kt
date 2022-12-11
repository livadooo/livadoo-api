package com.livadoo.common.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.URI

@ResponseStatus(HttpStatus.BAD_REQUEST)
abstract class BadInputException(
    override val title: String,
    override val detail: String,
    override val statusCode: String = ErrorCodes.BAD_INPUT,
    override val type: URI = DEFAULT_TYPE,
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
) : AbstractThrowableProblem(title, detail)