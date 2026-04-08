package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.CartEntity
import java.util.Optional

interface CartRepository : JpaRepository<CartEntity, Long> {

    fun findByUserId(userId: Long): Optional<CartEntity>

    fun existsByUserId(userId: Long): Boolean
}