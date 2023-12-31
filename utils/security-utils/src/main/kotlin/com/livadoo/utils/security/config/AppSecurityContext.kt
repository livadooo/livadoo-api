package com.livadoo.utils.security.config

import com.livadoo.utils.exception.UnauthorizedException
import com.livadoo.utils.security.domain.AuthUser
import com.livadoo.utils.security.domain.SYSTEM_ACCOUNT
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder

@Configuration
class AppSecurityContext {
    private val authentication =
        ReactiveSecurityContextHolder.getContext()
            .map { it.authentication }

    suspend fun getCurrentAuthUser(): AuthUser {
        return authentication.awaitFirstOrNull()
            ?.let { authentication -> authentication.principal as AuthUser }
            ?: throw UnauthorizedException("You are not authenticated")
    }

    suspend fun getCurrentUserId(): String {
        return getCurrentAuthUser().username
    }

    suspend fun getCurrentUserIdOrDefault(): String {
        return try {
            getCurrentAuthUser().username
        } catch (exception: UnauthorizedException) {
            SYSTEM_ACCOUNT
        }
    }

    suspend fun isCurrentUserAnonymous(): Boolean {
        return authentication.awaitFirstOrNull() is AnonymousAuthenticationToken
    }

    suspend fun currentUserHasPermissions(permissions: List<String>): Boolean {
        return getCurrentAuthUser()
            .authorities
            .map(GrantedAuthority::getAuthority)
            .containsAll(permissions)
    }
}
