package ru.alexandr.orderservice.dto.order

import ru.alexandr.orderservice.dto.enum.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderItemResponse(
    val productArticle: String,
    val productName: String,
    val quantity: Int,
    val price: BigDecimal,
    val lineTotal: BigDecimal
)

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val createdAt: LocalDateTime,
    val items: List<OrderItemResponse>
)