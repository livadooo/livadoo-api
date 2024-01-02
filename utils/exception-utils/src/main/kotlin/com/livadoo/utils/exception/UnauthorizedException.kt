package com.livadoo.utils.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
open class UnauthorizedException(detail: String = "You are not authenticated") : RuntimeException(detail)
