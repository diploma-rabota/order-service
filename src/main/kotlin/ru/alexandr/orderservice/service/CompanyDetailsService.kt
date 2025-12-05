package ru.alexandr.orderservice.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.repository.CompanyRepository

@Service
class CompanyDetailsService(
    private val companyRepository: CompanyRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return  companyRepository.findByInn(username)
            ?: throw UsernameNotFoundException("Company not found")
    }
}