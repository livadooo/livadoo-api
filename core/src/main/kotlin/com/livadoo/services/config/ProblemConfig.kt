package com.livadoo.services.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.WebExceptionHandler
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.spring.webflux.advice.ProblemExceptionHandler
import org.zalando.problem.spring.webflux.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblemModule

@Component
class ProblemConfig {

    @Bean
    fun problemModule(): ProblemModule {
        return ProblemModule()
    }

    @Bean
    fun constraintViolationProblemModule(): ConstraintViolationProblemModule {
        return ConstraintViolationProblemModule()
    }

    @Bean
    @Order(-2)
    fun problemExceptionHandler(
        mapper: ObjectMapper,
        problemHandling: ProblemHandling
    ): WebExceptionHandler {
        return ProblemExceptionHandler(mapper, problemHandling)
    }
}