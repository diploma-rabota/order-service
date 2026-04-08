package ru.alexandr.orderservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "cart_item")
@SequenceGenerator(
    name = "cart_item_seq_gen",
    sequenceName = "sqn_cart_item",
    allocationSize = 1
)
data class CartItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_seq_gen")
    val id: Long? = null,

    @Column(name = "cart_id", nullable = false)
    val cartId: Long,

    @Column(name = "product_article", nullable = false, length = 100)
    val productArticle: String,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)