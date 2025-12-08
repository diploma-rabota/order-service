package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.Cart

interface CartRepository: JpaRepository<Cart, Long> {

    fun findByUserIdAndCompanyId(userId: Long, companyId: Long): Cart?
}