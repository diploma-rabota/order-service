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
import ru.alexandr.orderservice.util.jwt.UnitType
import java.time.LocalDateTime

@Entity
@Table(name = "products")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqn_products")
    @SequenceGenerator(
        name = "sqn_products",
        sequenceName = "sqn_products",
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

    @Column(name = "price_wholesale")
    val priceWholesale: Long,

    @Column(name = "min_quantity_wholesale")
    val minQuantityWholesale: Long,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)