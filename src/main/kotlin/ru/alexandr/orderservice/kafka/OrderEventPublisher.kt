package ru.alexandr.orderservice.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Component
class OrderEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) {

    fun publishOrderCreated(event: OrderCreatedEvent) {
        val payload = objectMapper.writeValueAsString(event)
        kafkaTemplate.send("notification_orders", event.orderId.toString(), payload)
    }
}

data class OrderCreatedEvent(
    val eventId: UUID,
    val orderId: Long,
    val userId: Long,
    val email: String,
    val customerName: String,
    val totalAmount: BigDecimal,
    val createdAt: LocalDateTime,
)