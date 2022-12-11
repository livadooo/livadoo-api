package com.livadoo.services.advice.network

import com.livadoo.services.advice.AdviceTrait
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.SocketTimeoutException

interface SocketTimeoutAdviceTrait : AdviceTrait {

    @ExceptionHandler
    fun handleSocketTimeout(
        exception: SocketTimeoutException,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return handleException(
            exception,
            null,
            HttpHeaders(),
            HttpStatus.GATEWAY_TIMEOUT,
            request
        )
    }
}