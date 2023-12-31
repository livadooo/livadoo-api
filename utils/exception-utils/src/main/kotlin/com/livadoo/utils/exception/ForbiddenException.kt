package com.livadoo.utils.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
abstract class ForbiddenException(detail: String) : RuntimeException(detail)
