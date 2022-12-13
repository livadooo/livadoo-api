package com.livadoo.services.advice

import com.livadoo.services.advice.security.SecurityAdviceTrait
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.URI

private val LOG: Logger = LoggerFactory.getLogger(ExceptionHandling::class.java)

@RestControllerAdvice
class ExceptionHandling @Autowired constructor(

) : SecurityAdviceTrait, ResponseEntityExceptionHandler() {

    override fun handleException(
        exception: Exception,
        body: Any?,
        headers: HttpHeaders?,
        status: HttpStatusCode,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return handleExceptionInternal(exception, body, headers, status, exchange)
    }

    override fun handleExceptionInternal(
        exception: Exception,
        body: Any?,
        headers: HttpHeaders?,
        status: HttpStatusCode,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        val reasonPhrase = HttpStatus.valueOf(status.value()).reasonPhrase
        if (status.is4xxClientError) {
            LOG.warn("{}: {}", reasonPhrase, exception.message)
        } else if (status.is5xxServerError) {
            LOG.error(reasonPhrase, exception)
        }
        val errorBody = if(status.value() == 404) {
            ProblemDetail.forStatus(HttpStatus.NOT_FOUND)
                .apply {
                    type = URI.create("")
                    title = null
                    instance = null
                    setProperty("code", "404000")
                    setProperty("message", "404 NOT_FOUND")
                }
        } else body
        return super.handleExceptionInternal(exception, errorBody, headers, status, exchange)
    }
}
