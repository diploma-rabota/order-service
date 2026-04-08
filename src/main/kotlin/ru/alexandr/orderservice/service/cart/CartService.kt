package ru.alexandr.orderservice.service.cart

import ru.alexandr.orderservice.dto.cart.AddCartItemRequest
import ru.alexandr.orderservice.dto.cart.CartResponse
import ru.alexandr.orderservice.dto.cart.UpdateCartItemQuantityRequest

interface CartService {

    fun getCart(userId: Long): CartResponse

    fun addItem(userId: Long, request: AddCartItemRequest): CartResponse

    fun updateItemQuantity(
        userId: Long,
        productArticle: String,
        request: UpdateCartItemQuantityRequest
    ): CartResponse

    fun removeItem(userId: Long, productArticle: String): CartResponse

    fun clearCart(userId: Long)
}