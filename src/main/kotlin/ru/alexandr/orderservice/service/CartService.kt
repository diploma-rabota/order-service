package ru.alexandr.orderservice.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.config.security.SecurityUtils.getEmail
import ru.alexandr.orderservice.controller.AddToCartRequest
import ru.alexandr.orderservice.entity.Cart
import ru.alexandr.orderservice.entity.CartItem
import ru.alexandr.orderservice.entity.PriceTier
import ru.alexandr.orderservice.repository.CartItemRepository
import ru.alexandr.orderservice.repository.CartRepository
import ru.alexandr.orderservice.repository.PriceTierRepository
import ru.alexandr.orderservice.repository.ProductRepository
import ru.alexandr.orderservice.repository.UserRepository

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val priceTierRepository: PriceTierRepository,
) {

    @Transactional
    fun addToCart(request: AddToCartRequest): Long {
        val product = productRepository.findById(request.productId)
            .orElseThrow { IllegalArgumentException("Product not found") }

        require(request.quantity <= product.availableQuantity) {
            "Requested quantity ${request.quantity} exceeds available ${product.availableQuantity}"
        }

        val userEmail = getEmail() ?: throw IllegalStateException("User not authenticated")
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalStateException("User not found")

        val unitPrice = calculateUnitPrice(priceTierRepository.findPriceTiersByProductId(product.id),
            request.quantity)?: product.priceRetail

        val totalItemSum = unitPrice * request.quantity

        val cart = cartRepository.findByUserIdAndCompanyId(user.id, product.companyId)

        if (cart != null) {
            val newSumm = cart.totalSum * totalItemSum
            cartRepository.updateTotalSumById(cart.id, newSumm)

            cartItemRepository.save(createCartItem(cart.id, product.id, request.quantity))

            return cart.id
        }

        val newCart = cartRepository.save(
            Cart(
                userId = user.id,
                companyId = product.companyId,
                totalSum = totalItemSum
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


    private fun calculateUnitPrice(wholesalePrices: List<PriceTier>, qty: Long): Long? {
        val wholesale = wholesalePrices
            .filter { qty >= it.quantityFrom }
            .maxByOrNull { it.quantityFrom }

        return wholesale?.pricePerUnit
    }


}