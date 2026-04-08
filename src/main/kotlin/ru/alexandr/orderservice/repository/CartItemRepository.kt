package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.CartItemEntity

interface CartItemRepository : JpaRepository<CartItemEntity, Long> {

    fun findAllByCartId(cartId: Long): List<CartItemEntity>

    fun findByCartIdAndProductArticle(cartId: Long, productArticle: String): CartItemEntity?

    fun existsByCartId(cartId: Long): Boolean

    fun deleteByCartIdAndProductArticle(cartId: Long, productArticle: String)

    fun deleteAllByCartId(cartId: Long)
}