package com.livadoo.services.user.security

import com.livadoo.library.security.config.SecurityProperties
import com.livadoo.library.security.domain.AuthUser
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

private const val AUTHORITIES_KEY = "roles"
private const val AUTHORITIES_DELIMITER = ","

@Service
class JwtSigner(
    private val securityProperties: SecurityProperties
) {

    private val logger: Logger = LoggerFactory.getLogger(JwtSigner::class.java)

    fun createAccessToken(authentication: Authentication): String {
        val authUser = (authentication.principal as AuthUser)
        val authorities = authUser.authorities.joinToString(AUTHORITIES_DELIMITER) { it.authority }

        val tokenValidityInMilliSeconds = securityProperties.tokenValidityInSeconds * 1000
        val validity = Date(System.currentTimeMillis() + tokenValidityInMilliSeconds)

        return Jwts.builder()
            .setSubject(authUser.username)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(accessTokenKey, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .serializeToJsonWith(JacksonSerializer())
            .compact()
    }

    fun createRefreshToken(authentication: Authentication): String {
        val authUser = (authentication.principal as AuthUser)

        val tokenValidityInMilliSeconds = securityProperties.refreshTokenValidityInSeconds * 1000
        val validity = Date(System.currentTimeMillis() + tokenValidityInMilliSeconds)

        return Jwts.builder()
            .setSubject(authUser.username)
            .signWith(refreshTokenKey, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .serializeToJsonWith(JacksonSerializer())
            .compact()
    }

    fun getUserId(refreshToken: String): String? {
        val jwtParser = Jwts.parserBuilder().setSigningKey(refreshTokenKey).build()
        return try {
            val claims = jwtParser.parseClaimsJws(refreshToken).body
            claims.subject
        } catch (e: JwtException) {
            logger.info("Received invalid refresh token.")
            null
        }
    }

    private val accessTokenKey: SecretKey
        get() {
            val secret = securityProperties.secret
            return hmacShaKeyFor(secret.toByteArray())
        }

    private val refreshTokenKey: SecretKey
        get() {
            val secret = securityProperties.refreshSecret
            return hmacShaKeyFor(secret.toByteArray())
        }
}