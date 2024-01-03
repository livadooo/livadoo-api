package com.livadoo.services.authority.search

interface AuthoritySearchService {
    suspend fun getAuthoritiesByUserId(userId: String): AuthorityDto
}
