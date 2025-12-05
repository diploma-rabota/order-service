package ru.alexandr.orderservice.controller

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alexandr.orderservice.service.CompanyRegistrationService
import ru.alexandr.orderservice.util.JwtUtil

@RestController
@RequestMapping("/api")
class CompanyRegistrationController(
    private val authenticationManager: AuthenticationManager,
    private val registrationService: CompanyRegistrationService,
    private val jwtUtil: JwtUtil
) {

    data class RegistrationRequest(val inn: String, val name: String, val bik: String, val password: String)
    data class JwtResponse(val token: String)

    @PostMapping("/register")
    fun register(@RequestBody request: RegistrationRequest): JwtResponse {
        registrationService.register(request)
        val token = jwtUtil.generateToken(request.inn)
        return JwtResponse(token)
    }

    data class LoginRequest(val inn: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): JwtResponse {
         authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.inn, request.password)
        )
        val token = jwtUtil.generateToken(request.inn)
        return JwtResponse(token)
    }
}