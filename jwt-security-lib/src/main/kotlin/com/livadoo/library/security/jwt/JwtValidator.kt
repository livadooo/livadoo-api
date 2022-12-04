package com.livadoo.library.security.jwt

import com.livadoo.library.security.domain.AuthUser
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.security.Key

private const val AUTHORITIES_KEY = "roles"
private const val AUTHORITIES_DELIMITER = ","

class JwtValidator constructor(
    secret: String
) {

    private val logger: Logger = LoggerFactory.getLogger(JwtValidator::class.java)

    private val key: Key
    private val jwtParser: JwtParser

    init {
        val secretInBytes: ByteArray = secret.toByteArray()
        key = Keys.hmacShaKeyFor(secretInBytes)
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build()
    }

    fun getAuthentication(authToken: String): Authentication {
        val claims = jwtParser.parseClaimsJws(authToken).body

        val authorities = claims[AUTHORITIES_KEY]
            .toString()
            .split(AUTHORITIES_DELIMITER)
            .filter { authority -> authority.trim().isNotEmpty() }
            .map { SimpleGrantedAuthority(it) }
            .toList()
        val principal = AuthUser(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, authToken, authorities)
    }

    fun validateToken(authToken: String): Boolean {
        return try {
            jwtParser.parseClaimsJws(authToken)
            true
        } catch (e: JwtException) {
            logger.info("Received invalid JWT token.")
            false
        }
    }
}
