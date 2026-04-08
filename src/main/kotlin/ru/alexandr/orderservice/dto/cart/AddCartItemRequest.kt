package ru.alexandr.orderservice.dto.cart

data class AddCartItemRequest(
    val productArticle: String,
    val quantity: Int
)