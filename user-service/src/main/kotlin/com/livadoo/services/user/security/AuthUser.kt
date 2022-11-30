package com.livadoo.services.user.security

import com.fasterxml.jackson.annotation.JsonProperty

private const val AUTH_TOKEN_PREFIX = "Bearer "

data class AuthUserDTO(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String
)

class Login(val email: String, val password: String)
