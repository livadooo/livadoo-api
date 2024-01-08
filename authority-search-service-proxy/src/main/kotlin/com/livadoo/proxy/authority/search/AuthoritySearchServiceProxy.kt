package com.livadoo.proxy.authority.search

interface AuthoritySearchServiceProxy {
    suspend fun getUserAuthorities(userId: String): ProxyAuthorityDto
}
