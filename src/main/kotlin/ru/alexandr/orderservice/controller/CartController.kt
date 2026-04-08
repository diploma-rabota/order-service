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

    @GetMapping
    fun getCart(): CartResponse {
        return cartService.getCart()
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    fun addItem(
        @RequestBody request: AddCartItemRequest
    ): CartResponse {
        return cartService.addItem(request)
    }

    @PutMapping("/items/{productArticle}")
    fun updateItemQuantity(
        @PathVariable productArticle: String,
        @RequestBody request: UpdateCartItemQuantityRequest
    ): CartResponse {
        return cartService.updateItemQuantity(productArticle, request)
    }

    @DeleteMapping("/items/{productArticle}")
    fun removeItem(
        @PathVariable productArticle: String
    ): CartResponse {
        return cartService.removeItem(productArticle)
    }

    @DeleteMapping("/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun clearCart() {
        cartService.clearCart()
    }
}