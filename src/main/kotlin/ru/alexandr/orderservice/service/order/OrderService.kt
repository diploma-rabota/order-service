package ru.alexandr.orderservice.service.order

import ru.alexandr.orderservice.dto.order.OrderResponse
import ru.alexandr.orderservice.dto.order.UpdateOrderStatusRequest

interface OrderService {

    fun checkout(userId: Long): OrderResponse

    fun getOrderById(orderId: Long): OrderResponse

    fun getOrdersByUserId(userId: Long): List<OrderResponse>

    fun updateStatus(orderId: Long, request: UpdateOrderStatusRequest): OrderResponse
}