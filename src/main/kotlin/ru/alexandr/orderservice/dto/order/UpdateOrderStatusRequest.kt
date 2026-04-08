package ru.alexandr.orderservice.dto.order

import ru.alexandr.orderservice.dto.enum.OrderStatus

data class UpdateOrderStatusRequest(
    val status: OrderStatus
)