package ru.alexandr.orderservice.controller

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alexandr.orderservice.service.RegistrationService
import ru.alexandr.orderservice.util.jwt.JwtUtil

@RestController
@RequestMapping("/api")
class CompanyRegistrationController(
    private val registrationService: RegistrationService,
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegistrationRequest): JwtResponse {
        return registrationService.register(request)
    }

    @PostMapping("/register-company")
    fun registerCompany(@RequestBody request: RegisterCompanyRequest) {
         registrationService.registerCompany(request)
    }

    @PostMapping("/login")
    fun testLogin(@RequestBody request: LoginRequest): JwtResponse {
         return registrationService.login(request)
    }
}

data class RegistrationRequest(
    val username: String,
    val password: String,
    val email: String,
    val address: String
)

data class RegisterCompanyRequest(
    val inn: String,
    val bik: String,
    val name: String,
    val address: String
)


data class JwtResponse(val token: String)

data class LoginRequest(val email: String, val password: String)
