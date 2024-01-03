package com.livadoo.services.authority.search

import org.springframework.stereotype.Service

@Service
class DefaultAuthoritySearchService : AuthoritySearchService {
    override suspend fun getAuthoritiesByUserId(userId: String): AuthorityDto {
        TODO("Not yet implemented")
    }
}
