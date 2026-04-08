package ru.alexandr.orderservice.controller

import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.alexandr.orderservice.dto.ErrorResponse
import ru.alexandr.orderservice.exception.UnauthorizedException
import ru.alexandr.orderservice.exception.UserNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    message = ex.message ?: "Пользователь не найден"
                )
            )
    }


    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(mapOf("message" to (ex.message ?: "Unauthorized")))
    }

    @ExceptionHandler(FeignException::class)
    fun handleFeignException(ex: FeignException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(
                ErrorResponse(
                    status = HttpStatus.BAD_GATEWAY.value(),
                    message = "Ошибка при вызове внешнего сервиса"
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleCommonException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = ex.message ?: "Внутренняя ошибка сервера"
                )
            )
    }
}