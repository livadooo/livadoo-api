package com.livadoo.services.common.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
abstract class ObjectNotFoundException(detail: String) : RuntimeException(detail)