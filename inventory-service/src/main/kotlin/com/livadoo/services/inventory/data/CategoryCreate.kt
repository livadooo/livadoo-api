package com.livadoo.services.inventory.data

import com.fasterxml.jackson.annotation.JsonProperty

data class CategoryCreate (
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("parentId") val parentId: String?
)