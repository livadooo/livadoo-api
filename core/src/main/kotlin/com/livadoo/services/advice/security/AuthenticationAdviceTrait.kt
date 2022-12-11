package com.livadoo.services.advice.security

import com.livadoo.services.advice.AdviceTrait
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

interface AuthenticationAdviceTrait : AdviceTrait {

    @ExceptionHandler
    fun handleAuthentication(
        exception: AuthenticationException,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return handleException(
            exception,
            createProblemDetail(),
            HttpHeaders().apply { contentType = MediaType.APPLICATION_PROBLEM_JSON },
            HttpStatus.UNAUTHORIZED,
            request
        )
    }

    private fun createProblemDetail(): ProblemDetail =
        ProblemDetail
            .forStatus(HttpStatus.UNAUTHORIZED)
            .apply {
                title = "Unauthorized"
                detail = "You are not authenticated"
                setProperty("status_code", "401010")
            }
}