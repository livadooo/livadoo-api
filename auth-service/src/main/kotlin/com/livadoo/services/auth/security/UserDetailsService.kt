package com.livadoo.services.auth.security

import com.livadoo.services.auth.AuthRepository
import com.livadoo.utils.exception.UnauthorizedException
import com.livadoo.utils.security.domain.AuthUser
import com.livadoo.utils.user.UserEntity
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component("userDetailsService")
class UserDetailsService(
    private val authRepository: AuthRepository,
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return mono {
            authRepository.findByEmailIgnoreCase(username)
                ?.let { userEntity ->
                    if (userEntity.blocked) {
                        throw UnauthorizedException("Account has been blocked.")
                    }
                    if (userEntity.deleted) {
                        throw UnauthorizedException("Incorrect email or password")
                    }
                    createSpringSecurityUser(userEntity)
                }
                ?: throw UnauthorizedException("Incorrect email or password")
        }
    }

    private fun createSpringSecurityUser(userEntity: UserEntity): AuthUser {
        if (!userEntity.verified) {
            throw UnauthorizedException("Account not verified")
        }

        val permissions = userEntity.permissionIds
            .map { SimpleGrantedAuthority(it) }

        return AuthUser(
            username = userEntity.userId,
            userEntity.password!!,
            permissions = permissions,
            roles = userEntity.roleIds,
        )
    }
}
