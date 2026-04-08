package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.OrderItemEntity

interface OrderItemRepository : JpaRepository<OrderItemEntity, Long> {

    fun findAllByOrderId(orderId: Long): List<OrderItemEntity>

    fun findAllByOrderIdIn(orderIds: List<Long>): List<OrderItemEntity>
}