package com.livadoo.services.advice.validation

import com.livadoo.services.advice.AdviceTrait
import com.livadoo.services.advice.violations.Violation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

interface BaseValidationAdviceTrait: AdviceTrait {

    fun newConstraintViolationProblem(
        throwable: Exception,
        violations: List<Violation>,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        val status = HttpStatus.BAD_REQUEST
        val sortedViolations: List<Violation> = violations
            .sortedWith(Comparator.comparing(Violation::field).thenComparing(Violation::message))

        return handleException(
            throwable,
            toProblemDetail(sortedViolations, status),
            HttpHeaders(),
            status,
            request
        )
    }

    private fun toProblemDetail(violations: List<Violation>, status: HttpStatus): ProblemDetail {
        val problemDetail = ProblemDetail
            .forStatusAndDetail(status, "Some fields were not resolved")
        problemDetail.title = "Validation error"
        problemDetail.setProperty("status_code", "400010")
        problemDetail.setProperty("violations", violations)

        return problemDetail
    }
}