package ru.alexandr.orderservice.util.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {
    private val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val expiration = 1000 * 60 * 60 * 24 // 24 часа

    fun generateToken(inn: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)
        return Jwts.builder()
            .setSubject(inn)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).body.subject
        } catch (ex: Exception) {
            null
        }
    }

    fun validateToken(token: String): Boolean {
        return extractUsername(token) != null
    }
}