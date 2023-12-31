package com.livadoo.utils.security.jwt

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

private const val AUTHORIZATION_HEADER = "Authorization"
private const val AUTH_TOKEN_PREFIX = "Bearer "

class JwtFilter(
    private val jwtSigner: JwtValidator,
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        val authToken = extractToken(exchange.request)
        if (StringUtils.hasText(authToken) && jwtSigner.validateToken(authToken!!)) {
            val authentication = jwtSigner.getAuthentication(authToken)
            val context = ReactiveSecurityContextHolder.withAuthentication(authentication)
            return chain.filter(exchange).contextWrite(context)
        }
        return chain.filter(exchange)
    }

    private fun extractToken(request: ServerHttpRequest): String? {
        val bearerToken = request.headers.getFirst(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearerToken) && bearerToken!!.startsWith(AUTH_TOKEN_PREFIX)) {
            return bearerToken.removePrefix(AUTH_TOKEN_PREFIX)
        }
        return null
    }
}
