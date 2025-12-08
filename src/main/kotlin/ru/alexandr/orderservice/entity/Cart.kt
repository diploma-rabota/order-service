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
@Table(name = "cart")
class Cart(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqn_cart")
    @SequenceGenerator(
        name = "sqn_cart",
        sequenceName = "sqn_cart",
        allocationSize = 1
    )
    val id: Long = 0,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "company_id")
    val companyId: Long,

    @Column
    val totalSum: Long,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,

)