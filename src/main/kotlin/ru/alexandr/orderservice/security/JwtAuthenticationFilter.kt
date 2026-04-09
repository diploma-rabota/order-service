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


        val authHeader = request.getHeader("Authorization")

        val beforeAuth = SecurityContextHolder.getContext().authentication

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)


            val currentAuth = SecurityContextHolder.getContext().authentication
            val shouldAuthenticate = currentAuth == null || currentAuth is AnonymousAuthenticationToken

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

                } else {
                }
            }
        }

        filterChain.doFilter(request, response)

    }
}