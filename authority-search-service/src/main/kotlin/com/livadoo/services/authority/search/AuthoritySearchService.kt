package com.livadoo.services.authority.search

interface AuthoritySearchService {
    suspend fun getUserAuthorities(userId: String): AuthorityDto
}
