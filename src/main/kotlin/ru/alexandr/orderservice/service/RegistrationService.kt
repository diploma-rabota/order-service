package ru.alexandr.orderservice.service
import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.controller.JwtResponse
import ru.alexandr.orderservice.controller.RegisterCompanyRequest
import ru.alexandr.orderservice.controller.RegistrationRequest
import ru.alexandr.orderservice.entity.Company
import ru.alexandr.orderservice.entity.User
import ru.alexandr.orderservice.repository.CompanyRepository
import ru.alexandr.orderservice.repository.UserRepository
import ru.alexandr.orderservice.util.jwt.JwtUtil

@Service
class RegistrationService(
    private val repository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) {
    fun register(request: RegistrationRequest): JwtResponse {
        require(repository.findByEmail(request.email) == null)
        val user = User(
            email = request.email,
            userName = request.username,
            userPassword = passwordEncoder.encode(request.password).toString(),
            address = request.address,
        )

        val savedCompany = repository.save(user)
        val token = jwtUtil.generateToken(savedCompany.email)
        return JwtResponse(token)
    }

    @Transactional
    fun registerCompany(request: RegisterCompanyRequest){
        val email = SecurityContextHolder.getContext().authentication?.name
        val user = repository.findByEmail(email!!)

        val company = Company(
            inn = request.inn,
            bik = request.bik,
            name = request.name,
            address = request.address,
        )
        companyRepository.save(company)

        repository.save(user!!.copy(
            companyId = company.id,
        ))
    }

}