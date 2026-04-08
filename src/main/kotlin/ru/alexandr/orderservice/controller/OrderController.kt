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

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    fun checkout(): OrderResponse {
        return orderService.checkout()
    }

    @GetMapping("/{orderId}")
    fun getOrderById(
        @PathVariable orderId: Long
    ): OrderResponse {
        return orderService.getOrderById(orderId)
    }

    @GetMapping("/my")
    fun getMyOrders(): List<OrderResponse> {
        return orderService.getMyOrders()
    }

    @PatchMapping("/{orderId}/status")
    fun updateStatus(
        @PathVariable orderId: Long,
        @RequestBody request: UpdateOrderStatusRequest
    ): OrderResponse {
        return orderService.updateStatus(orderId, request)
    }
}