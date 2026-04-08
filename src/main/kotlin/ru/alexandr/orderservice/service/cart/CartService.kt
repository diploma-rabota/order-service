package ru.alexandr.orderservice.service.cart

import ru.alexandr.orderservice.dto.cart.AddCartItemRequest
import ru.alexandr.orderservice.dto.cart.CartResponse
import ru.alexandr.orderservice.dto.cart.UpdateCartItemQuantityRequest

interface CartService {

    fun getCart(): CartResponse

    fun addItem(request: AddCartItemRequest): CartResponse

    fun updateItemQuantity(
        productArticle: String,
        request: UpdateCartItemQuantityRequest
    ): CartResponse

    fun removeItem(productArticle: String): CartResponse

    fun clearCart()
}