package ru.alexandr.orderservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alexandr.orderservice.service.CartService

@RestController
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService
) {


    @PostMapping("/add-product")
    fun createProductForCompany(@RequestBody request: AddToCartRequest){
        cartService.addToCart(request)
    }
}


data class AddToCartRequest(
    val productId: Long,
    val quantity: Long,
)