package com.livadoo.services.user.security

private const val AUTH_TOKEN_PREFIX = "Bearer "

data class AuthUserDTO(val accessToken: String, val refreshToken: String)

class Login(val email: String, val password: String)
