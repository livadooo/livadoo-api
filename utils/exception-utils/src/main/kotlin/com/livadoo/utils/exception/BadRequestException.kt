package com.livadoo.utils.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
abstract class BadRequestException(detail: String) : RuntimeException(detail)
