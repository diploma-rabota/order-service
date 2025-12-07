package ru.alexandr.orderservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alexandr.orderservice.entity.Product

interface ProductRepository: JpaRepository<Product, Long> {
}