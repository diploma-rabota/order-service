package ru.alexandr.orderservice.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.alexandr.orderservice.dto.cart.AddCartItemRequest
import ru.alexandr.orderservice.dto.cart.CartResponse
import ru.alexandr.orderservice.dto.cart.UpdateCartItemQuantityRequest
import ru.alexandr.orderservice.service.cart.CartService

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {

    @GetMapping("/{userId}")
    fun getCart(
        @PathVariable userId: Long
    ): CartResponse {
        return cartService.getCart(userId)
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    fun addItem(
        @PathVariable userId: Long,
        @RequestBody  request: AddCartItemRequest
    ): CartResponse {
        return cartService.addItem(userId, request)
    }

    @PutMapping("/{userId}/items/{productArticle}")
    fun updateItemQuantity(
        @PathVariable userId: Long,
        @PathVariable productArticle: String,
        @RequestBody  request: UpdateCartItemQuantityRequest
    ): CartResponse {
        return cartService.updateItemQuantity(userId, productArticle, request)
    }

    @DeleteMapping("/{userId}/items/{productArticle}")
    fun removeItem(
        @PathVariable userId: Long,
        @PathVariable productArticle: String
    ): CartResponse {
        return cartService.removeItem(userId, productArticle)
    }

    @DeleteMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun clearCart(
        @PathVariable userId: Long
    ) {
        cartService.clearCart(userId)
    }
}