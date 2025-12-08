package ru.alexandr.orderservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "price_tier")
data class PriceTier(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqn_price")
    @SequenceGenerator(
        name = "sqn_price",
        sequenceName = "sqn_price",
        allocationSize = 1
    )
    val id: Long = 0,

    @Column
    val productId: Long,

    @Column()
    val quantityFrom: Long,

    @Column
    val pricePerUnit: Long,
)