package ru.alexandr.orderservice.service
import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.config.security.CustomUserDetails
import ru.alexandr.orderservice.config.security.SecurityUtils.getEmail
import ru.alexandr.orderservice.controller.JwtResponse
import ru.alexandr.orderservice.controller.LoginRequest
import ru.alexandr.orderservice.controller.RegisterCompanyRequest
import ru.alexandr.orderservice.controller.RegistrationRequest
import ru.alexandr.orderservice.entity.Company
import ru.alexandr.orderservice.entity.User
import ru.alexandr.orderservice.entity.toCustomUserDetails
import ru.alexandr.orderservice.repository.CompanyRepository
import ru.alexandr.orderservice.repository.UserRepository
import ru.alexandr.orderservice.util.jwt.JwtUtil

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager,
    ) {
    fun register(request: RegistrationRequest): JwtResponse {
        require(userRepository.findByEmail(request.email) == null)
        val user = User(
            email = request.email,
            userName = request.username,
            userPassword = passwordEncoder.encode(request.password).toString(),
            address = request.address,
        )

        val savedUser = userRepository.save(user)
        val token = jwtUtil.generateToken(savedUser.toCustomUserDetails())
        return JwtResponse(token)
    }

    @Transactional
    fun registerCompany(request: RegisterCompanyRequest){
        val email = getEmail()
        val user = userRepository.findByEmail(email!!)

        val company = Company(
            inn = request.inn,
            bik = request.bik,
            name = request.name,
            address = request.address,
        )
        companyRepository.save(company)

        userRepository.save(user!!.copy(
            companyId = company.id,
        ))
    }

    fun login(request: LoginRequest): JwtResponse {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = auth.principal as CustomUserDetails

        val token = jwtUtil.generateToken(user)
        return JwtResponse(token)
    }

}