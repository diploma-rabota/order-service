package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.OrderEntity

interface OrderRepository : JpaRepository<OrderEntity, Long> {

    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long): List<OrderEntity>
}