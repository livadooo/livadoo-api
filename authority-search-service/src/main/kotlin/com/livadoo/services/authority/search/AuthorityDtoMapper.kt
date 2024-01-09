package com.livadoo.services.authority.search

import org.bson.Document

fun Document.toAuthorityDto(): AuthorityDto = AuthorityDto(
    roles = getList("roles", String::class.java),
    permissions = getList("permissions", String::class.java),
)
