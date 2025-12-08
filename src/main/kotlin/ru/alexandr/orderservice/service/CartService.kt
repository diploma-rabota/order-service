package ru.alexandr.orderservice.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.config.security.SecurityUtils.getEmail
import ru.alexandr.orderservice.controller.AddToCartRequest
import ru.alexandr.orderservice.entity.Cart
import ru.alexandr.orderservice.entity.CartItem
import ru.alexandr.orderservice.repository.CartItemRepository
import ru.alexandr.orderservice.repository.CartRepository
import ru.alexandr.orderservice.repository.ProductRepository
import ru.alexandr.orderservice.repository.UserRepository

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun addToCart(request: AddToCartRequest): Long {
        val product = productRepository.findById(request.productId)
            .orElseThrow { IllegalArgumentException("Product not found") }

        val userEmail = getEmail() ?: throw IllegalStateException("User not authenticated")
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalStateException("User not found")

        // 1️⃣ Проверяем количество товара
        if (product.availableQuantity < request.quantity) {
            throw IllegalArgumentException(
                "Requested quantity ${request.quantity} exceeds available ${product.availableQuantity}"
            )
        }

        // 2️⃣ Ищем корзину ДЛЯ ЭТОЙ компании
        val cartForThisCompany =
            cartRepository.findByUserIdAndCompanyId(user.id, product.companyId)

        // 3️⃣ Если корзина найдена → кладём туда
        if (cartForThisCompany != null) {
            cartItemRepository.save(createCartItem(cartForThisCompany.id, product.id, request.quantity))
            return cartForThisCompany.id
        }

        // 4️⃣ Корзины для этой компании нет → создаём новую
        val newCart = cartRepository.save(
            Cart(
                userId = user.id,
                companyId = product.companyId
            )
        )

        cartItemRepository.save(createCartItem(newCart.id, product.id, request.quantity))

        return newCart.id
    }

    private fun createCartItem(cartId: Long, productId: Long, quantity: Long) =
        CartItem(
            cartId = cartId,
            productId = productId,
            quantity = quantity,
        )

}