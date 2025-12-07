package ru.alexandr.orderservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import ru.alexandr.orderservice.config.security.CustomUserDetails

@Entity
@Table(name = "duser")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqn_user")
    @SequenceGenerator(
        name = "sqn_user",
        sequenceName = "sqn_user",
        allocationSize = 1
    )
    val id: Long = 0,

    @Column
    val userName: String,

    @Column(name = "password")
    var userPassword: String,

    @Column
    val companyId: Long? = null,

    @Column
    val email: String,

    @Column
    val address: String,
)

fun User.toCustomUserDetails() : CustomUserDetails {
    return CustomUserDetails(
        email = email,
        companyId = companyId,
        password = userPassword,
        )
}