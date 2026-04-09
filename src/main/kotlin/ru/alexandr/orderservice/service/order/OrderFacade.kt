package ru.alexandr.orderservice.service.order

import org.springframework.stereotype.Service
import ru.alexandr.orderservice.dto.order.OrderResponse
import ru.alexandr.orderservice.kafka.OrderCreatedEvent
import ru.alexandr.orderservice.kafka.OrderEventPublisher
import java.util.UUID

@Service
class OrderFacade(
    private val orderService: OrderService,
    private val orderEventPublisher: OrderEventPublisher,
) {

    fun checkout(): OrderResponse {
        val response = orderService.checkout()

        orderEventPublisher.publishOrderCreated(
            OrderCreatedEvent(
                eventId = UUID.randomUUID(),
                orderId = response.id,
                userId = response.userId,
                email = response.userEmail,
                totalAmount = response.totalAmount,
                createdAt = response.createdAt,
                customerName = response.userEmail.substringBefore("@"),
            )
        )

        return response
    }
}