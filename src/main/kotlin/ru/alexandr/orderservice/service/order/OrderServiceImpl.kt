package ru.alexandr.orderservice.service.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.alexandr.orderservice.client.CatalogClient
import ru.alexandr.orderservice.dto.CatalogProductDto
import ru.alexandr.orderservice.dto.cart.CatalogProductsByArticlesRequest
import ru.alexandr.orderservice.dto.order.OrderItemResponse
import ru.alexandr.orderservice.dto.order.OrderResponse
import ru.alexandr.orderservice.dto.order.UpdateOrderStatusRequest
import ru.alexandr.orderservice.entity.CartItemEntity
import ru.alexandr.orderservice.entity.OrderEntity
import ru.alexandr.orderservice.entity.OrderItemEntity
import ru.alexandr.orderservice.kafka.OrderCreatedEvent
import ru.alexandr.orderservice.kafka.OrderEventPublisher
import ru.alexandr.orderservice.repository.CartItemRepository
import ru.alexandr.orderservice.repository.CartRepository
import ru.alexandr.orderservice.repository.OrderItemRepository
import ru.alexandr.orderservice.repository.OrderRepository
import ru.alexandr.orderservice.security.CurrentUserProvider
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class OrderServiceImpl(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val catalogClient: CatalogClient,
    private val currentUserProvider: CurrentUserProvider,
) : OrderService {


    @Transactional
    override fun checkout(): OrderResponse {
        val currentUser = currentUserProvider.getCurrentUser()
        val now = LocalDateTime.now()

        val cart = cartRepository.findByUserId(currentUser.userId).orElseThrow {
            IllegalArgumentException("Корзина пользователя ${currentUser.userId} не найдена")
        }

        val cartItems = cartItemRepository.findAllByCartId(requireNotNull(cart.id))
        require(cartItems.isNotEmpty()) { "Нельзя оформить пустую корзину" }

        val articles = cartItems.map { it.productArticle }.distinct()

        val products = catalogClient.getProductsByArticles(
            CatalogProductsByArticlesRequest(articles = articles)
        )

        val productMap = products.associateBy { it.article }

        if (productMap.size != articles.size) {
            val missingArticles = articles.filterNot { productMap.containsKey(it) }
            throw IllegalArgumentException("Не найдены товары по артикулам: $missingArticles")
        }

        val orderItems = cartItems.map { cartItem ->
            val product = productMap[cartItem.productArticle]
                ?: throw IllegalArgumentException("Не найден товар с артикулом ${cartItem.productArticle}")

            buildOrderItem(
                cartItem = cartItem,
                product = product,
                createdAt = now,
            )
        }

        val totalAmount = orderItems.fold(BigDecimal.ZERO) { acc, item ->
            acc + item.lineTotal
        }

        val savedOrder = orderRepository.save(
            OrderEntity(
                userId = currentUser.userId,
                userEmail = currentUser.email,
                totalAmount = totalAmount,
                createdAt = now,
                updatedAt = now,
            )
        )

        val savedOrderItems = orderItemRepository.saveAll(
            orderItems.map { item ->
                item.copy(orderId = requireNotNull(savedOrder.id))
            }
        )

        cartItemRepository.deleteAllByCartId(requireNotNull(cart.id))
        cartRepository.save(cart.copy(updatedAt = now))

        return mapToOrderResponse(savedOrder, savedOrderItems)
    }

    @Transactional(readOnly = true)
    override fun getOrderById(orderId: Long): OrderResponse {
        val currentUserId = currentUserProvider.getCurrentUserId()

        val order = orderRepository.findById(orderId).orElseThrow {
            IllegalArgumentException("Заказ с id=$orderId не найден")
        }

        require(order.userId == currentUserId) {
            "Нет доступа к заказу другого пользователя"
        }

        val items = orderItemRepository.findAllByOrderId(orderId)
        return mapToOrderResponse(order, items)
    }

    @Transactional(readOnly = true)
    override fun getMyOrders(): List<OrderResponse> {
        val currentUserId = currentUserProvider.getCurrentUserId()

        val orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(currentUserId)
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
        val currentUserId = currentUserProvider.getCurrentUserId()

        val existingOrder = orderRepository.findById(orderId).orElseThrow {
            IllegalArgumentException("Заказ с id=$orderId не найден")
        }

        require(existingOrder.userId == currentUserId) {
            "Нет доступа к изменению чужого заказа"
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
            userEmail = order.userEmail,
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

    private fun buildOrderItem(
        cartItem: CartItemEntity,
        product: CatalogProductDto,
        createdAt: LocalDateTime,
    ): OrderItemEntity {
        require(product.isActive) {
            "Товар с артикулом ${product.article} недоступен для заказа"
        }

        require(cartItem.quantity > 0) {
            "Количество товара ${product.article} должно быть больше 0"
        }

        require(cartItem.quantity <= product.stockQuantity) {
            "Недостаточно товара ${product.article} на складе. " +
                    "Доступно: ${product.stockQuantity}, запрошено: ${cartItem.quantity}"
        }

        val unitPrice = resolveUnitPrice(
            quantity = cartItem.quantity,
            retailPrice = product.price,
            wholesalePrice = product.wholesalePrice,
            minWholesaleQuantity = product.minWholesaleQuantity,
        ).setScale(2, java.math.RoundingMode.HALF_UP)

        val lineTotal = unitPrice
            .multiply(BigDecimal.valueOf(cartItem.quantity.toLong()))
            .setScale(2, java.math.RoundingMode.HALF_UP)

        return OrderItemEntity(
            productArticle = product.article,
            productName = product.name,
            quantity = cartItem.quantity,
            price = unitPrice,
            lineTotal = lineTotal,
            createdAt = createdAt,
        )
    }

    private fun resolveUnitPrice(
        quantity: Int,
        retailPrice: BigDecimal,
        wholesalePrice: BigDecimal,
        minWholesaleQuantity: Int,
    ): BigDecimal {
        return if (quantity >= minWholesaleQuantity) wholesalePrice else retailPrice
    }
}