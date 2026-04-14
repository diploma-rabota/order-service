package ru.alexandr.orderservice.metric


import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component

@Component
class OrderMetrics(
    registry: MeterRegistry,
) {

    val checkoutTimer: Timer = Timer.builder("order.checkout.duration")
        .description("Order checkout processing duration")
        .publishPercentileHistogram()
        .register(registry)

    val checkoutErrors: Counter = Counter.builder("order.checkout.errors.total")
        .description("Order checkout errors")
        .register(registry)

    val cartOperationTimer: Timer = Timer.builder("order.cart.operation.duration")
        .description("Cart operations duration")
        .publishPercentileHistogram()
        .register(registry)

    val cartOperationErrors: Counter = Counter.builder("order.cart.operation.errors.total")
        .description("Cart operations errors")
        .register(registry)

    val catalogTimer: Timer = Timer.builder("order.internal.catalog.duration")
        .description("Calls to catalog service")
        .publishPercentileHistogram()
        .register(registry)

    val catalogErrors: Counter = Counter.builder("order.internal.catalog.errors.total")
        .register(registry)

    val userTimer: Timer = Timer.builder("order.internal.user.duration")
        .description("Calls to user service")
        .publishPercentileHistogram()
        .register(registry)

    val userErrors: Counter = Counter.builder("order.internal.user.errors.total")
        .register(registry)

    val kafkaPublish: Counter = Counter.builder("order.kafka.publish.total")
        .description("Published order events")
        .register(registry)

    val kafkaErrors: Counter = Counter.builder("order.kafka.publish.errors.total")
        .register(registry)
}