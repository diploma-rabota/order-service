package ru.alexandr.orderservice.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.alexandr.orderservice.service.UserDetailsService
import ru.alexandr.orderservice.util.jwt.JwtUtil

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            println("HEADER OK: $authHeader")

            val token = authHeader.substring(7)
            println("TOKEN = $token")

            val email = jwtUtil.extractUsername(token)
            println("EMAIL FROM TOKEN = $email")


            if (email != null && SecurityContextHolder.getContext().authentication == null) {

                val userDetails = userDetailsService.loadUserByUsername(email)
                println("USER DETAILS LOADED: ${userDetails.username}")

                if (jwtUtil.validateToken(token)) {

                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )

                    authToken.details =
                        WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        println("AUTH = " + SecurityContextHolder.getContext().authentication)

        filterChain.doFilter(request, response)
    }
}