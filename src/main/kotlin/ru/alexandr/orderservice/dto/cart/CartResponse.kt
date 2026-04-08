package ru.alexandr.orderservice.dto.cart

data class CartResponse(
    val userId: Long,
    val items: List<CartItemResponse>
)

data class CartItemResponse(
    val productArticle: String,
    val quantity: Int
)