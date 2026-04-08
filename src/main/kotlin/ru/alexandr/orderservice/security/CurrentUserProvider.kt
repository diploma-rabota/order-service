package ru.alexandr.orderservice.security


import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import ru.alexandr.orderservice.exception.UnauthorizedException

@Component
class CurrentUserProvider {

    fun getCurrentUser(): JwtUserPrincipal {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw UnauthorizedException("Пользователь не аутентифицирован")

        val principal = authentication.principal as? JwtUserPrincipal
            ?: throw UnauthorizedException("Не удалось извлечь данные пользователя из токена")

        return principal
    }

    fun getCurrentUserId(): Long = getCurrentUser().userId

    fun getCurrentUserEmail(): String = getCurrentUser().email
}