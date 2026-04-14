package ru.alexandr.orderservice.service.order

import org.springframework.stereotype.Service
import ru.alexandr.orderservice.dto.order.OrderResponse
import ru.alexandr.orderservice.kafka.OrderCreatedEvent
import ru.alexandr.orderservice.kafka.OrderEventPublisher
import ru.alexandr.orderservice.metric.OrderMetrics
import java.util.UUID

@Service
class OrderFacade(
    private val orderService: OrderService,
    private val orderEventPublisher: OrderEventPublisher,
    private val orderMetrics: OrderMetrics
) {

    fun checkout(): OrderResponse {
        return try {
            orderMetrics.checkoutTimer.recordCallable<OrderResponse> {
                val response = orderService.checkout()

                try {
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
                    orderMetrics.kafkaPublish.increment()
                } catch (ex: Exception) {
                    orderMetrics.kafkaErrors.increment()
                    throw ex
                }

                response
            }
        } catch (ex: Exception) {
            orderMetrics.checkoutErrors.increment()
            throw ex
        }
    }
}