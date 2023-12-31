package com.livadoo.utils.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
abstract class NotFoundException(detail: String) : RuntimeException(detail)
