package ru.alexandr.orderservice.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.repository.UserRepository

@Service
class UserDetailsService(
    private val repository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return  repository.findByEmail(username)
            ?: throw UsernameNotFoundException("Company not found")
    }
}