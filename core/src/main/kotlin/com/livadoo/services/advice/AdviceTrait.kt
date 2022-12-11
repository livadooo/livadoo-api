package com.livadoo.services.advice

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.lang.Exception

interface AdviceTrait {

    fun handleException(
        exception: Exception,
        body: Any?,
        headers: HttpHeaders?,
        status: HttpStatusCode,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>>
}