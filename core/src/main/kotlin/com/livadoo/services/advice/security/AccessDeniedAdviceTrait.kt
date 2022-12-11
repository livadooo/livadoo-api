package com.livadoo.services.advice.security

import com.livadoo.services.advice.AdviceTrait
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

interface AccessDeniedAdviceTrait : AdviceTrait {

    @ExceptionHandler
    fun handleAccessDenied(
        exception: AccessDeniedException,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        return handleException(
            exception,
            createProblemDetail(),
            HttpHeaders().apply { contentType = MediaType.APPLICATION_PROBLEM_JSON },
            HttpStatus.FORBIDDEN,
            request
        )
    }

    private fun createProblemDetail(): ProblemDetail =
        ProblemDetail
            .forStatus(HttpStatus.FORBIDDEN)
            .apply {
                title = "Forbidden"
                detail = "You are not allow to access this resource"
                setProperty("status_code", "403010")
            }
}