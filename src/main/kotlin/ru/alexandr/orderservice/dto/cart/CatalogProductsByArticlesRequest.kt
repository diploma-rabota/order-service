package ru.alexandr.orderservice.dto.cart


data class CatalogProductsByArticlesRequest(
    val articles: List<String>
)