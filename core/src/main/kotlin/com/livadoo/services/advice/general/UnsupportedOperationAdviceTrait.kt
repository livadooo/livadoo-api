package com.livadoo.services.advice.general

import com.livadoo.services.advice.AdviceTrait
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

interface UnsupportedOperationAdviceTrait : AdviceTrait {

    @ExceptionHandler
    fun handleUnsupportedOperation(
        exception: UnsupportedOperationException,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return handleException(
            exception,
            null,
            HttpHeaders(),
            HttpStatus.NOT_IMPLEMENTED,
            request
        )
    }
}