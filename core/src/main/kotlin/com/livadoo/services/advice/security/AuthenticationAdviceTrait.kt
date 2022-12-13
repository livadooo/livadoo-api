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
import java.net.URI

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
                type = URI.create("")
                title = null
                status = 401
                setProperty("code", "401000")
                setProperty("message", "You are not authenticated")
            }
}