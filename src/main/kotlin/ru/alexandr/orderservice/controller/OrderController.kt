package ru.alexandr.orderservice.controller


import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.alexandr.orderservice.dto.order.OrderResponse
import ru.alexandr.orderservice.dto.order.UpdateOrderStatusRequest
import ru.alexandr.orderservice.service.order.OrderService

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/{userId}/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    fun checkout(
        @PathVariable userId: Long
    ): OrderResponse {
        return orderService.checkout(userId)
    }

    @GetMapping("/{orderId}")
    fun getOrderById(
        @PathVariable orderId: Long
    ): OrderResponse {
        return orderService.getOrderById(orderId)
    }

    @GetMapping
    fun getOrdersByUserId(
        @RequestParam userId: Long
    ): List<OrderResponse> {
        return orderService.getOrdersByUserId(userId)
    }

    @PatchMapping("/{orderId}/status")
    fun updateStatus(
        @PathVariable orderId: Long,
        @RequestBody  request: UpdateOrderStatusRequest
    ): OrderResponse {
        return orderService.updateStatus(orderId, request)
    }
}