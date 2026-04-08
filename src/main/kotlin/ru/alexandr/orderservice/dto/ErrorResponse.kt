package ru.alexandr.orderservice.dto

data class ErrorResponse(
    val status: Int,
    val message: String
)