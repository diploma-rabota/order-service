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

) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(GrantedAuthority { "ROLE_USER" })

    override fun getPassword() = userPassword
    override fun getUsername() = email
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}