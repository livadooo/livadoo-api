package com.livadoo.services.authority.search

import com.livadoo.services.authority.search.UserAuthoritiesAggregationQuery.getUserAuthoritiesAggregationQuery
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service

@Service
class DefaultAuthoritySearchService(
    private val mongoTemplate: ReactiveMongoTemplate,
) : AuthoritySearchService {
    override suspend fun getUserAuthorities(userId: String): AuthorityDto {
        return mongoTemplate.getCollection("users")
            .awaitFirst()
            .aggregate(getUserAuthoritiesAggregationQuery(userId))
            .awaitFirstOrNull()
            ?.toAuthorityDto()
            ?: throw Exception("User not found")
    }
}
