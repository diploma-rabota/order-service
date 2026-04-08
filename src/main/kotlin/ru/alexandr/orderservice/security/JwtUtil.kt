package ru.alexandr.orderservice.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${security.jwt.secret}")
    secret: String
) {


    private val key: SecretKey =
        Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun extractUserId(token: String): Long? =
        runCatching {
            when (val value = extractAllClaims(token)["userId"]) {
                is Int -> value.toLong()
                is Long -> value
                is String -> value.toLong()
                else -> null
            }
        }.getOrNull()

    fun extractEmail(token: String): String? =
        runCatching {
            when (val value = extractAllClaims(token)["email"]) {
                is String -> value
                else -> extractAllClaims(token).subject
            }
        }.getOrNull()

    fun validateToken(token: String): Boolean =
        runCatching {
            val claims = extractAllClaims(token)
            claims.subject != null && !claims.expiration.before(Date())
        }.getOrDefault(false)

    private fun extractAllClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}