package com.livadoo.services.user.services.mongodb.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "authorities")
data class AuthorityEntity(@Id var name: String)