package ru.alexandr.orderservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "order_item")
@SequenceGenerator(
    name = "order_item_seq_gen",
    sequenceName = "sqn_order_item",
    allocationSize = 1
)
data class OrderItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq_gen")
    val id: Long? = null,

    @Column(name = "order_id", nullable = false)
    val orderId: Long? = null,

    @Column(name = "product_article", nullable = false, length = 100)
    val productArticle: String,

    @Column(name = "product_name", nullable = false, length = 255)
    val productName: String,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    val price: BigDecimal,

    @Column(name = "line_total", nullable = false, precision = 19, scale = 2)
    val lineTotal: BigDecimal,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)