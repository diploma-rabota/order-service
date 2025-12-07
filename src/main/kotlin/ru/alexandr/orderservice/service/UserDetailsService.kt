package ru.alexandr.orderservice.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.config.security.CustomUserDetails
import ru.alexandr.orderservice.repository.UserRepository

@Service
class UserDetailsService(
    private val repository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = repository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found")

        return CustomUserDetails(
            email = user.email,
            password = user.userPassword,
            companyId = user.companyId,
        )
    }
}