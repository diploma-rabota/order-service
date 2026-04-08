package ru.alexandr.orderservice.dto.cart

import java.math.BigDecimal

data class CatalogProductDto(
    val article: String,
    val name: String,
    val price: BigDecimal
)

data class CatalogProductsByArticlesRequest(
    val articles: List<String>
)