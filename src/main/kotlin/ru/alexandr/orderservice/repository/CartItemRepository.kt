package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.CartItem

interface CartItemRepository: JpaRepository<CartItem, Long> {
}