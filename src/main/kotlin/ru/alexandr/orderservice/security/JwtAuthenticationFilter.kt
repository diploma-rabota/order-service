package ru.alexandr.orderservice.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("JWT FILTER START")
        println("URI = ${request.requestURI}")

        val authHeader = request.getHeader("Authorization")
        println("AUTH HEADER = $authHeader")

        val beforeAuth = SecurityContextHolder.getContext().authentication
        println("AUTH BEFORE = $beforeAuth")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            println("TOKEN VALID = ${jwtUtil.validateToken(token)}")
            println("TOKEN USER_ID = ${jwtUtil.extractUserId(token)}")
            println("TOKEN EMAIL = ${jwtUtil.extractEmail(token)}")

            val currentAuth = SecurityContextHolder.getContext().authentication
            val shouldAuthenticate = currentAuth == null || currentAuth is AnonymousAuthenticationToken
            println("SHOULD AUTHENTICATE = $shouldAuthenticate")

            if (shouldAuthenticate && jwtUtil.validateToken(token)) {
                val userId = jwtUtil.extractUserId(token)
                val email = jwtUtil.extractEmail(token)

                if (userId != null && email != null) {
                    val principal = JwtUserPrincipal(
                        userId = userId,
                        email = email
                    )

                    val authToken = UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        emptyList()
                    )

                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken

                    println("AUTH SET = ${SecurityContextHolder.getContext().authentication}")
                } else {
                    println("AUTH NOT SET: userId or email is null")
                }
            }
        }

        filterChain.doFilter(request, response)

        println("AUTH AFTER CHAIN = ${SecurityContextHolder.getContext().authentication}")
        println("JWT FILTER END")
    }
}