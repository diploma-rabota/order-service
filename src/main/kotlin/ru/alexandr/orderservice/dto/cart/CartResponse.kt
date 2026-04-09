package ru.alexandr.orderservice.dto.cart

import java.math.BigDecimal

data class CartResponse(
    val userId: Long,
    val items: List<CartItemResponse>
)

data class CartItemResponse(
    val productArticle: String,
    val productName: String?,
    val quantity: Int,
    val price: BigDecimal?,
    val stockQuantity: Int?,
    val active: Boolean,
    val available: Boolean
)