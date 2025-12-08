package ru.alexandr.orderservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import ru.alexandr.orderservice.util.UnitType
import java.time.LocalDateTime

@Entity
@Table(name = "product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqn_product")
    @SequenceGenerator(
        name = "sqn_product",
        sequenceName = "sqn_product",
        allocationSize = 1
    )
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column
    val categoryId: Long,

    @Column
    val companyId: Long,

    @Column(name = "available_quantity")
    val availableQuantity: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val unit: UnitType,

    @Column(name = "price_retail")
    val priceRetail: Long,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)