package ru.alexandr.orderservice.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.alexandr.orderservice.service.CompanyDetailsService
import ru.alexandr.orderservice.util.JwtUtil

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val companyDetailsService: CompanyDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            val inn = jwtUtil.extractUsername(token)
            if (inn != null && SecurityContextHolder.getContext().authentication == null) {
                val companyDetails = companyDetailsService.loadUserByUsername(inn)
                if (jwtUtil.validateToken(token)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        companyDetails, null, companyDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}