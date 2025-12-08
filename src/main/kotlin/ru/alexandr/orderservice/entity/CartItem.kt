package ru.alexandr.orderservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "cart_item")
class CartItem(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqn_cart_item")
    @SequenceGenerator(
        name = "sqn_cart_item",
        sequenceName = "sqn_cart_item",
        allocationSize = 1
    )
    val id: Long = 0,

    @Column(name = "cart_id")
    val cartId: Long,

    @Column(name = "product_id")
    val productId: Long,

    @Column
    val quantity: Long
)