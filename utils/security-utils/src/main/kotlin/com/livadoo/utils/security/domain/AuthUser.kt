package com.livadoo.utils.security.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

data class AuthUser(
    @get:JvmName("getUserId") val username: String,
    @get:JvmName("getUserPassword") val password: String,
    val permissions: List<GrantedAuthority>,
    val roles: List<String>,
) : User(username, password, permissions)
