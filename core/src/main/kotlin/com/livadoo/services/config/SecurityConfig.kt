package com.livadoo.services.config

import com.livadoo.library.security.jwt.JwtFilter
import com.livadoo.library.security.jwt.JwtValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.stereotype.Component
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Import(SecurityProblemSupport::class)
@Component()
class SecurityConfig @Autowired constructor(
    private val jwtValidator: JwtValidator,
    private val problemSupport: SecurityProblemSupport,
    private val userDetailsService: ReactiveUserDetailsService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
        authenticationManager.setPasswordEncoder(passwordEncoder())
        return authenticationManager
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        // @formatter:off
        return http
            .securityMatcher(
                NegatedServerWebExchangeMatcher(
                    OrServerWebExchangeMatcher(
                        ServerWebExchangeMatchers.pathMatchers(HttpMethod.OPTIONS, "/**")
                    )
                )
            )
            .csrf().disable()
            .addFilterAt(JwtFilter(jwtValidator), SecurityWebFiltersOrder.HTTP_BASIC)
            .exceptionHandling()
            .accessDeniedHandler(problemSupport)
            .authenticationEntryPoint(problemSupport)
            .and()
            .requestCache()
            .requestCache(NoOpServerRequestCache.getInstance())
            .and()
            .authorizeExchange { authorize ->
                authorize
                    .pathMatchers("/v1/users/verify").permitAll()
                    .pathMatchers("/v1/users/customer").permitAll()
                    .pathMatchers("/v1/users/login").permitAll()
                    .pathMatchers("/v1/users/token/refresh").permitAll()
                    .pathMatchers("/v1/users/reset-password").permitAll()
                    .pathMatchers("/v1/users/request-password-reset").permitAll()
                    .pathMatchers(HttpMethod.GET, "/v1/products").permitAll()
                    .pathMatchers("/v1/products/*").permitAll()
                    .pathMatchers("/v1/files/**").permitAll()
                    .pathMatchers("/**").authenticated()
            }
            .build()
        // @formatter:on
    }
}
