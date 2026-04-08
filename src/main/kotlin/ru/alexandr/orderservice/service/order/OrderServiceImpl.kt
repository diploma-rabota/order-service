package ru.alexandr.orderservice.service.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.alexandr.orderservice.client.CatalogClient
import ru.alexandr.orderservice.dto.cart.CatalogProductsByArticlesRequest
import ru.alexandr.orderservice.dto.order.OrderItemResponse
import ru.alexandr.orderservice.dto.order.OrderResponse
import ru.alexandr.orderservice.dto.order.UpdateOrderStatusRequest
import ru.alexandr.orderservice.entity.OrderEntity
import ru.alexandr.orderservice.entity.OrderItemEntity
import ru.alexandr.orderservice.repository.CartItemRepository
import ru.alexandr.orderservice.repository.CartRepository
import ru.alexandr.orderservice.repository.OrderItemRepository
import ru.alexandr.orderservice.repository.OrderRepository
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class OrderServiceImpl(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val catalogClient: CatalogClient
) : OrderService {

    @Transactional
    override fun checkout(userId: Long): OrderResponse {
        val now = LocalDateTime.now()

        val cart = cartRepository.findByUserId(userId).orElseThrow {
            IllegalArgumentException("Корзина пользователя $userId не найдена")
        }

        val cartItems = cartItemRepository.findAllByCartId(requireNotNull(cart.id))
        require(cartItems.isNotEmpty()) { "Нельзя оформить пустую корзину" }

        val articles = cartItems.map { it.productArticle }.distinct()
        val products = catalogClient.getProductsByArticles(CatalogProductsByArticlesRequest(articles = articles))

        val productMap = products.associateBy { it.article }

        if (productMap.size != articles.size) {
            val missingArticles = articles.filterNot { productMap.containsKey(it) }
            throw IllegalArgumentException("Не найдены товары по артикулам: $missingArticles")
        }

        val orderItems = cartItems.map { cartItem ->
            val product = productMap[cartItem.productArticle]
                ?: throw IllegalArgumentException("Не найден товар с артикулом ${cartItem.productArticle}")

            val lineTotal = product.price.multiply(BigDecimal.valueOf(cartItem.quantity.toLong()))

            OrderItemEntity(
                productArticle = product.article,
                productName = product.name,
                quantity = cartItem.quantity,
                price = product.price,
                lineTotal = lineTotal,
                createdAt = now,
            )
        }

        val totalAmount = orderItems.fold(BigDecimal.ZERO) { acc, item ->
            acc + item.lineTotal
        }

        val savedOrder = orderRepository.save(
            OrderEntity(
                userId = userId,
                totalAmount = totalAmount,
                createdAt = now,
                updatedAt = now
            )
        )

        val savedOrderItems = orderItemRepository.saveAll(
            orderItems.map {
                it.copy(orderId = requireNotNull(savedOrder.id))
            }
        )

        cartItemRepository.deleteAllByCartId(requireNotNull(cart.id))
        cartRepository.save(cart.copy(updatedAt = now))

        return mapToOrderResponse(savedOrder, savedOrderItems)
    }

    @Transactional(readOnly = true)
    override fun getOrderById(orderId: Long): OrderResponse {
        val order = orderRepository.findById(orderId).orElseThrow {
            IllegalArgumentException("Заказ с id=$orderId не найден")
        }

        val items = orderItemRepository.findAllByOrderId(orderId)
        return mapToOrderResponse(order, items)
    }

    @Transactional(readOnly = true)
    override fun getOrdersByUserId(userId: Long): List<OrderResponse> {
        val orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
        if (orders.isEmpty()) {
            return emptyList()
        }

        val orderIds = orders.mapNotNull { it.id }
        val orderItems = orderItemRepository.findAllByOrderIdIn(orderIds)
        val itemsByOrderId = orderItems.groupBy { it.orderId }

        return orders.map { order ->
            mapToOrderResponse(
                order = order,
                items = itemsByOrderId[requireNotNull(order.id)].orEmpty()
            )
        }
    }

    @Transactional
    override fun updateStatus(orderId: Long, request: UpdateOrderStatusRequest): OrderResponse {
        val existingOrder = orderRepository.findById(orderId).orElseThrow {
            IllegalArgumentException("Заказ с id=$orderId не найден")
        }

        val updatedOrder = orderRepository.save(
            existingOrder.copy(
                status = request.status,
                updatedAt = LocalDateTime.now()
            )
        )

        val items = orderItemRepository.findAllByOrderId(orderId)
        return mapToOrderResponse(updatedOrder, items)
    }

    private fun mapToOrderResponse(
        order: OrderEntity,
        items: List<OrderItemEntity>
    ): OrderResponse {
        return OrderResponse(
            id = requireNotNull(order.id),
            userId = order.userId,
            status = order.status,
            totalAmount = order.totalAmount,
            createdAt = order.createdAt,
            items = items.map { item ->
                OrderItemResponse(
                    productArticle = item.productArticle,
                    productName = item.productName,
                    quantity = item.quantity,
                    price = item.price,
                    lineTotal = item.lineTotal
                )
            }
        )
    }
}