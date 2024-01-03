package com.livadoo.proxy.authority.search

interface AuthoritySearchServiceProxy {
    suspend fun getAuthoritiesByUserId(userId: String): ProxyAuthorityDto
}
