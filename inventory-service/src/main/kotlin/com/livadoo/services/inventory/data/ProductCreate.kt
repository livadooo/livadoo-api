package com.livadoo.services.inventory.data

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductCreate (
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("categoryId") val categoryId: String,
    @JsonProperty("quantity") val quantity: Int,
    @JsonProperty("price") val price: Float
)