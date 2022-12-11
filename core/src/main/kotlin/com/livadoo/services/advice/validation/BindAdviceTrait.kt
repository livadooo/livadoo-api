package com.livadoo.services.advice.validation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

interface BindAdviceTrait: BaseBindingResultAdviceTrait {

    fun handleBindingResult(
        exception: WebExchangeBindException,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return newConstraintViolationProblem(exception, createViolations(exception), request)
    }
}