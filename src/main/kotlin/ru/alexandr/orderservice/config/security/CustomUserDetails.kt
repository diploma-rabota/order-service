package ru.alexandr.orderservice.config.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val email: String,
    val companyId: Long?,
    private val password: String,
) : UserDetails {

    override fun getUsername() = email
    override fun getPassword() = password
    override fun getAuthorities()  =
        mutableListOf(GrantedAuthority { "ROLE_USER" })

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}



object SecurityUtils {
    fun getCompanyId(): Long? {
        val auth = SecurityContextHolder.getContext().authentication ?: return null
        val principal = auth.principal
        return if (principal is CustomUserDetails) principal.companyId else null
    }

    fun getEmail(): String? {
        val auth = SecurityContextHolder.getContext().authentication ?: return null
        val principal = auth.principal
        return if (principal is CustomUserDetails) principal.username else null
    }
}

