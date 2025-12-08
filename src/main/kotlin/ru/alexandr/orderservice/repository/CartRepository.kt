package ru.alexandr.orderservice.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.alexandr.orderservice.entity.Cart

interface CartRepository: JpaRepository<Cart, Long> {

    @Modifying
    @Query(
        value = "UPDATE cart SET total_sum = :totalSum WHERE id = :id",
        nativeQuery = true
    )
    fun updateTotalSumById(id: Long, totalSum: Long)

    fun findByUserIdAndCompanyId(userId: Long, companyId: Long): Cart?
}