package ru.alexandr.orderservice.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.controller.JwtResponse
import ru.alexandr.orderservice.controller.RegistrationRequest
import ru.alexandr.orderservice.entity.User
import ru.alexandr.orderservice.repository.UserRepository
import ru.alexandr.orderservice.util.jwt.JwtUtil

@Service
class RegistrationService(
    private val repository: UserRepository,
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

}