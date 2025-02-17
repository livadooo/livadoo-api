package com.livadoo.services.auth.security

import com.livadoo.utils.security.config.SecurityProperties
import com.livadoo.utils.security.domain.AuthUser
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

private const val ROLES_KEY = "roles"
private const val PERMISSIONS_KEY = "permissions"
private const val DELIMITER = ","

@Service
class JwtSigner(
    private val securityProperties: SecurityProperties,
) {
    private val logger: Logger = LoggerFactory.getLogger(JwtSigner::class.java)

    fun createAccessToken(authentication: Authentication): String {
        val authUser = (authentication.principal as AuthUser)
        val permissions = authUser.authorities.joinToString(DELIMITER) { it.authority }
        val roles = authUser.roles.joinToString(DELIMITER)

        val tokenValidityInMilliSeconds = securityProperties.tokenValidityInSeconds * 1000
        val validity = Date(System.currentTimeMillis() + tokenValidityInMilliSeconds)
        val accessTokenKey = accessTokenKey
        return Jwts.builder()
            .subject(authUser.username)
            .claim(PERMISSIONS_KEY, permissions)
            .claim(ROLES_KEY, roles)
            .signWith(accessTokenKey, Jwts.SIG.HS512)
            .expiration(validity)
            .json(JacksonSerializer())
            .compact()
    }

    fun createRefreshToken(authentication: Authentication): String {
        val authUser = (authentication.principal as AuthUser)

        val tokenValidityInMilliSeconds = securityProperties.refreshTokenValidityInSeconds * 1000
        val validity = Date(System.currentTimeMillis() + tokenValidityInMilliSeconds)

        return Jwts.builder()
            .subject(authUser.username)
            .signWith(refreshTokenKey, Jwts.SIG.HS512)
            .expiration(validity)
            .json(JacksonSerializer())
            .compact()
    }

    fun getUserId(refreshToken: String): String? {
        val jwtParser = Jwts.parser().verifyWith(refreshTokenKey).build()
        return try {
            val claims = jwtParser.parseSignedClaims(refreshToken).payload
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
