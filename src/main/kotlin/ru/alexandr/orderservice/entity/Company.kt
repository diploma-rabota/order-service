package ru.alexandr.orderservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "company")
data class Company(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @SequenceGenerator(
        name = "company_seq",
        sequenceName = "sqn_company",
        allocationSize = 1
    )
    val id: Long = 0,

    @Column
    val inn: String,

    @Column
    val bik: String,

    @Column
    val name: String,

    @Column
    val address: String,
)