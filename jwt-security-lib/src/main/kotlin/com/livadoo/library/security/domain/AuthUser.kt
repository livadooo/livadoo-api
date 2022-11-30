package com.livadoo.library.security.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AuthUser(
    username: String,
    password: String,
    authorities: List<GrantedAuthority>
) : User(username, password, authorities) {
    val isCustomer: Boolean
        get() = authorities.firstOrNull { it.authority == ROLE_CUSTOMER } != null

    val isAdmin: Boolean
        get() = authorities.firstOrNull { it.authority == ROLE_ADMIN || it.authority == ROLE_EDITOR } != null
}
