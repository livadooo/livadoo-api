package com.livadoo.services.advice

import com.fasterxml.jackson.databind.ObjectMapper
import com.livadoo.services.advice.security.SecurityAdviceTrait
import com.livadoo.services.advice.utils.AdviceUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityProblemSupport @Autowired constructor (
    val advice: SecurityAdviceTrait,
    val mapper: ObjectMapper
): ServerAuthenticationEntryPoint, ServerAccessDeniedHandler {

    override fun commence(exchange: ServerWebExchange, exception: AuthenticationException): Mono<Void> {
        return advice.handleAuthentication(exception, exchange)
            .flatMap { entity -> AdviceUtils.setHttpResponse(entity, exchange, mapper) }
    }

    override fun handle(exchange: ServerWebExchange, exception: AccessDeniedException): Mono<Void> {
        return advice.handleAccessDenied(exception, exchange)
            .flatMap { entity -> AdviceUtils.setHttpResponse(entity, exchange, mapper) }
    }
}