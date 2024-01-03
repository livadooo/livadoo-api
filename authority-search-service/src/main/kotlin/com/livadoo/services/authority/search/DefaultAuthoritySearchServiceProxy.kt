package com.livadoo.services.authority.search

import com.livadoo.proxy.authority.search.AuthoritySearchServiceProxy
import com.livadoo.proxy.authority.search.ProxyAuthorityDto
import org.springframework.stereotype.Service

@Service
class DefaultAuthoritySearchServiceProxy(
    private val authoritySearchService: AuthoritySearchService,
) : AuthoritySearchServiceProxy {
    override suspend fun getAuthoritiesByUserId(userId: String): ProxyAuthorityDto {
        return authoritySearchService.getAuthoritiesByUserId(userId).toProxy()
    }

    private fun AuthorityDto.toProxy(): ProxyAuthorityDto {
        return ProxyAuthorityDto(roles = roles, permissions = permissions)
    }
}
