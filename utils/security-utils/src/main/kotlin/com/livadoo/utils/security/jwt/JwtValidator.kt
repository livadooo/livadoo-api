package com.livadoo.utils.security.jwt

import com.livadoo.utils.security.domain.AuthUser
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.security.Key

private const val ROLES_KEY = "roles"
private const val PERMISSIONS_KEY = "permissions"
private const val DELIMITER = ","

class JwtValidator(
    secret: String,
) {
    private val logger = LoggerFactory.getLogger(JwtValidator::class.java)
    private val key: Key
    private val jwtParser: JwtParser

    init {
        val secretInBytes: ByteArray = secret.toByteArray()
        key = Keys.hmacShaKeyFor(secretInBytes)
        jwtParser = Jwts.parser().verifyWith(key).build()
    }

    fun getAuthentication(authToken: String): Authentication {
        val claims = jwtParser.parseSignedClaims(authToken).payload

        val roles =
            claims[ROLES_KEY]
                .toString()
                .split(DELIMITER)
                .filter { authority -> authority.trim().isNotEmpty() }

        val permissions =
            claims[PERMISSIONS_KEY]
                .toString()
                .split(DELIMITER)
                .toMutableList()
                .apply { addAll(roles) }
                .filter { authority -> authority.trim().isNotEmpty() }
                .map { SimpleGrantedAuthority(it) }

        val principal = AuthUser(username = claims.subject, password = "", permissions = permissions, roles = roles)

        return UsernamePasswordAuthenticationToken(principal, authToken, permissions)
    }

    fun validateToken(authToken: String): Boolean {
        return try {
            jwtParser.parseSignedClaims(authToken)
            true
        } catch (e: JwtException) {
            logger.info("Received invalid JWT token.")
            false
        }
    }
}
