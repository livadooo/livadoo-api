package com.livadoo.services.user.security

import com.livadoo.common.exceptions.UnauthorizedException
import com.livadoo.library.security.domain.AuthUser
import com.livadoo.services.user.services.mongodb.entity.UserEntity
import com.livadoo.services.user.services.mongodb.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component("userDetailsService")
class UserDetailsService @Autowired constructor(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {

    private val logger: Logger = LoggerFactory.getLogger(UserDetailsService::class.java)

    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository
            .findByEmailIgnoreCase(username)
            .switchIfEmpty { throw UnauthorizedException("Incorrect email or password") }
            .flatMap { userEntity ->
                if (userEntity.blocked) {
                    logger.info("User with id: ${userEntity.id} tried to login while his account being blocked")
                    return@flatMap Mono.error(UnauthorizedException("Account blocked, please contact administration"))
                }
                if (userEntity.deleted) {
                    logger.info("User with id: ${userEntity.id} tried to login while his account being deleted")
                    return@flatMap Mono.error(UnauthorizedException("Incorrect email or password"))
                }
                createSpringSecurityUser(userEntity)
            }
    }

    private fun createSpringSecurityUser(user: UserEntity): Mono<AuthUser> {
        if (!user.verified) {
            logger.info("User with id: ${user.id} tried to login while his account is not verified")
            throw UnauthorizedException("Account not verified")
        }

        val authorities: List<GrantedAuthority> = user.authorities
            .map { SimpleGrantedAuthority(it.name) }

        return AuthUser(user.id!!, user.password, authorities).toMono()
    }
}