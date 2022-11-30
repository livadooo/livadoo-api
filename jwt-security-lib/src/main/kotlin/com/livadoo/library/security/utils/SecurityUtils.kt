package com.livadoo.library.security.utils

import com.livadoo.library.security.domain.AuthUser
import com.livadoo.library.security.domain.ROLE_ANONYMOUS
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Mono

private val authentication = ReactiveSecurityContextHolder
    .getContext()
    .map { it.authentication }

val currentAuthUser: Mono<AuthUser> = authentication.flatMap { authentication ->
    val nullableAuthUser = authentication.principal as? AuthUser
    Mono.justOrEmpty(nullableAuthUser)
}

val currentUserId: Mono<String> = currentAuthUser
    .map { it.username }

val currentUserAuthToken: Mono<String> = authentication
    .filter { it.credentials is String }
    .map { it.credentials as String }

val isAuthenticated: Mono<Boolean> = authentication
    .map { it.authorities }
    .map { authorities ->
        !authorities
            .map { it.authority }
            .contains(ROLE_ANONYMOUS)
    }

fun hasCurrentUserThisAuthority(authority: String): Mono<Boolean> = authentication
    .map { it.authorities }
    .map { authorities ->
        authorities
            .map { it.authority }
            .contains(authority)
    }

val authenticationFilter: ExchangeFilterFunction
	get() = ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
		currentUserAuthToken.map { authToken ->
			ClientRequest.from(clientRequest)
				.headers { it.setBearerAuth(authToken) }
				.build()
		}
	}
