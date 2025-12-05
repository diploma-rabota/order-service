package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.User

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}