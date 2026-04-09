package ru.alexandr.orderservice.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CatalogProductDto(
    val article: String,
    val name: String,
    val price: BigDecimal,
    val wholesalePrice: BigDecimal,
    val minWholesaleQuantity: Int,
    val stockQuantity: Int,
    @param:JsonProperty("active")
    val isActive: Boolean
)