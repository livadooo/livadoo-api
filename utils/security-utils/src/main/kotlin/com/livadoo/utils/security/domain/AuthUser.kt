package com.livadoo.utils.security.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AuthUser(
    username: String,
    password: String,
    authorities: List<GrantedAuthority>,
) : User(username, password, authorities) {
    val isCustomer: Boolean
        get() = authorities.firstOrNull { it.authority == ROLE_CUSTOMER } != null

    val isStaff: Boolean
        get() = STAFF_ROLES.any { role -> role in authorities.map { it.authority } }

    val isAdmin: Boolean
        get() = ADMIN_ROLES.any { role -> role in authorities.map { it.authority } }
}
