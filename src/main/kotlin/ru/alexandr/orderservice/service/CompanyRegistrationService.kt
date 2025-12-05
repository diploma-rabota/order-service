package ru.alexandr.orderservice.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.controller.JwtResponse
import ru.alexandr.orderservice.controller.RegistrationRequest
import ru.alexandr.orderservice.entity.Company
import ru.alexandr.orderservice.repository.CompanyRepository
import ru.alexandr.orderservice.util.JwtUtil

@Service
class CompanyRegistrationService(
    private val companyRepository: CompanyRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) {
    fun register(request: RegistrationRequest): JwtResponse {
        require(companyRepository.findByInn(request.inn) == null)

        val company = Company(
            inn = request.inn,
            name = request.name,
            bik = request.bik,
            companyPassword = passwordEncoder.encode(request.password).toString()
        )


        val savedCompany = companyRepository.save(company)
        val token = jwtUtil.generateToken(savedCompany.inn)
        return JwtResponse(token)
    }

}