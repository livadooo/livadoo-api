package com.livadoo.services.advice.general

import com.livadoo.common.exceptions.AbstractThrowableProblem
import com.livadoo.services.advice.AdviceTrait
import org.springframework.http.HttpHeaders
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

interface ThrowableAdviceTrait : AdviceTrait {

    @ExceptionHandler
    fun handleThrowable(problem: AbstractThrowableProblem, request: ServerWebExchange): Mono<ResponseEntity<Any>> {
        return handleException(
            problem,
            toProblemDetail(problem),
            HttpHeaders(),
            problem.status,
            request
        )
    }

    private fun toProblemDetail(problem: AbstractThrowableProblem): ProblemDetail {
        val problemDetail = ProblemDetail
            .forStatusAndDetail(problem.status, problem.detail)
        problemDetail.title = problem.title
        problemDetail.setProperty("status_code", problem.statusCode)

        return problemDetail
    }
}