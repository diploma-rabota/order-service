package ru.alexandr.orderservice.service.order

import ru.alexandr.orderservice.dto.order.OrderResponse
import ru.alexandr.orderservice.dto.order.UpdateOrderStatusRequest

interface OrderService {

    fun checkout(): OrderResponse

    fun getOrderById(orderId: Long): OrderResponse

    fun getMyOrders(): List<OrderResponse>

    fun updateStatus(orderId: Long, request: UpdateOrderStatusRequest): OrderResponse
}