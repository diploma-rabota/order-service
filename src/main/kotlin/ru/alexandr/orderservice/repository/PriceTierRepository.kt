package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.PriceTier

interface PriceTierRepository: JpaRepository<PriceTier, Long> {
    fun findPriceTiersByProductId(productId: Long): List<PriceTier>
}