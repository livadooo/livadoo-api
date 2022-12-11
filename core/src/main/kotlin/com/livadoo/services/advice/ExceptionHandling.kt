package com.livadoo.services.advice

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.livadoo.services.advice.security.SecurityAdviceTrait
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

private val LOG: Logger = LoggerFactory.getLogger(ExceptionHandling::class.java)

@RestControllerAdvice
class ExceptionHandling : ProblemHandling, SecurityAdviceTrait, ResponseEntityExceptionHandler() {

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
        return super.handleExceptionInternal(exception, body, headers, status, exchange)
    }

    override fun handleServerWebInputException(
        ex: ServerWebInputException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return when (val cause = ex.rootCause) {
            is DecodingException -> {
                val message = "Missing required request body"
                val body = ProblemDetail.forStatusAndDetail(status, message)
                return handleExceptionInternal(Exception(message), body, headers, status, exchange)
            }

            is MissingKotlinParameterException -> {
                val message = "Missing required field: ${cause.parameter.name}"
                val body = ProblemDetail.forStatusAndDetail(status, message)
                return handleExceptionInternal(Exception(message), body, headers, status, exchange)
            }

            else -> super.handleServerWebInputException(ex, headers, status, exchange)
        }
    }

    override fun handleWebExchangeBindException(
        ex: WebExchangeBindException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return handleBindingResult(ex, exchange)
    }
}
