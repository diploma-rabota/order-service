package ru.alexandr.orderservice.service.cart

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.alexandr.orderservice.client.CatalogClient
import ru.alexandr.orderservice.client.UserClient
import ru.alexandr.orderservice.dto.CatalogProductDto
import ru.alexandr.orderservice.dto.cart.AddCartItemRequest
import ru.alexandr.orderservice.dto.cart.CartItemResponse
import ru.alexandr.orderservice.dto.cart.CartResponse
import ru.alexandr.orderservice.dto.cart.CatalogProductsByArticlesRequest
import ru.alexandr.orderservice.dto.cart.UpdateCartItemQuantityRequest
import ru.alexandr.orderservice.entity.CartEntity
import ru.alexandr.orderservice.entity.CartItemEntity
import ru.alexandr.orderservice.repository.CartItemRepository
import ru.alexandr.orderservice.repository.CartRepository
import ru.alexandr.orderservice.security.CurrentUserProvider
import java.time.LocalDateTime

@Service
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val currentUserProvider: CurrentUserProvider,
    private val catalogClient: CatalogClient,
    private val userClient: UserClient,
) : CartService {

    @Transactional(readOnly = true)
    override fun getCart(): CartResponse {
        val userId = getValidatedCurrentUserId()

        val cart = cartRepository.findByUserId(userId).orElse(null)
            ?: return emptyCart(userId)

        return buildCartResponse(cart)
    }

    @Transactional
    override fun addItem(request: AddCartItemRequest): CartResponse {
        val userId = getValidatedCurrentUserId()

        validateProductArticle(request.productArticle)
        validateQuantity(request.quantity)

        val now = LocalDateTime.now()
        val cart = getOrCreateCart(userId, now)

        val existingItem = cartItemRepository.findByCartIdAndProductArticle(
            cartId = requireNotNull(cart.id),
            productArticle = request.productArticle
        )

        val newQuantity = if (existingItem == null) {
            request.quantity
        } else {
            existingItem.quantity + request.quantity
        }

        val product = getCatalogProductOrThrow(request.productArticle)
        validateCatalogProductAvailability(product, newQuantity)

        if (existingItem == null) {
            cartItemRepository.save(
                CartItemEntity(
                    cartId = requireNotNull(cart.id),
                    productArticle = request.productArticle,
                    quantity = request.quantity,
                    createdAt = now,
                    updatedAt = now
                )
            )
        } else {
            cartItemRepository.save(
                existingItem.copy(
                    quantity = newQuantity,
                    updatedAt = now
                )
            )
        }

        touchCart(cart, now)

        return buildCartResponseByUserId(userId)
    }

    @Transactional
    override fun updateItemQuantity(
        productArticle: String,
        request: UpdateCartItemQuantityRequest
    ): CartResponse {
        val userId = getValidatedCurrentUserId()

        validateProductArticle(productArticle)
        validateQuantity(request.quantity)

        val now = LocalDateTime.now()
        val cart = getExistingCart(userId)

        val existingItem = cartItemRepository.findByCartIdAndProductArticle(
            cartId = requireNotNull(cart.id),
            productArticle = productArticle
        ) ?: throw IllegalArgumentException("Товар с артикулом $productArticle не найден в корзине")

        val product = getCatalogProductOrThrow(productArticle)
        validateCatalogProductAvailability(product, request.quantity)

        cartItemRepository.save(
            existingItem.copy(
                quantity = request.quantity,
                updatedAt = now
            )
        )

        touchCart(cart, now)

        return buildCartResponseByUserId(userId)
    }

    @Transactional
    override fun removeItem(productArticle: String): CartResponse {
        val userId = getValidatedCurrentUserId()

        validateProductArticle(productArticle)

        val cart = cartRepository.findByUserId(userId).orElse(null)
            ?: return emptyCart(userId)

        cartItemRepository.deleteByCartIdAndProductArticle(
            cartId = requireNotNull(cart.id),
            productArticle = productArticle
        )

        touchCart(cart, LocalDateTime.now())

        return buildCartResponseByUserId(userId)
    }

    @Transactional
    override fun clearCart() {
        val userId = getValidatedCurrentUserId()

        val cart = cartRepository.findByUserId(userId).orElse(null) ?: return

        cartItemRepository.deleteAllByCartId(requireNotNull(cart.id))
        touchCart(cart, LocalDateTime.now())
    }

    private fun getValidatedCurrentUserId(): Long {
        val userId = currentUserProvider.getCurrentUserId()
        val userResponse = userClient.getUserEmail(userId)

        require(userResponse.email.isNotBlank()) {
            "Пользователь с id=$userId не найден"
        }

        return userId
    }

    private fun getOrCreateCart(userId: Long, now: LocalDateTime): CartEntity {
        return cartRepository.findByUserId(userId).orElseGet {
            cartRepository.save(
                CartEntity(
                    userId = userId,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }

    private fun getExistingCart(userId: Long): CartEntity {
        return cartRepository.findByUserId(userId).orElseThrow {
            IllegalArgumentException("Корзина пользователя $userId не найдена")
        }
    }

    private fun touchCart(cart: CartEntity, now: LocalDateTime) {
        cartRepository.save(cart.copy(updatedAt = now))
    }

    private fun buildCartResponseByUserId(userId: Long): CartResponse {
        val cart = getExistingCart(userId)
        return buildCartResponse(cart)
    }

    private fun buildCartResponse(cart: CartEntity): CartResponse {
        val cartItems = cartItemRepository.findAllByCartId(requireNotNull(cart.id))

        if (cartItems.isEmpty()) {
            return CartResponse(
                userId = cart.userId,
                items = emptyList()
            )
        }

        val articles = cartItems.map { it.productArticle }.distinct()
        val products = catalogClient.getProductsByArticles(
            CatalogProductsByArticlesRequest(articles = articles)
        )

        val productMap = products.associateBy { it.article }

        val items = cartItems.map { item ->
            val product = productMap[item.productArticle]

            val active = product?.isActive == true
            val available = product != null &&
                    product.isActive &&
                    item.quantity <= product.stockQuantity

            CartItemResponse(
                productArticle = item.productArticle,
                productName = product?.name,
                quantity = item.quantity,
                price = product?.price,
                stockQuantity = product?.stockQuantity,
                active = active,
                available = available
            )
        }

        return CartResponse(
            userId = cart.userId,
            items = items
        )
    }

    private fun emptyCart(userId: Long): CartResponse {
        return CartResponse(
            userId = userId,
            items = emptyList()
        )
    }

    private fun validateProductArticle(productArticle: String) {
        require(productArticle.isNotBlank()) {
            "Артикул товара не должен быть пустым"
        }
    }

    private fun validateQuantity(quantity: Int) {
        require(quantity > 0) {
            "Количество товара должно быть больше 0"
        }
    }

    private fun getCatalogProductOrThrow(article: String): CatalogProductDto {
        val products = catalogClient.getProductsByArticles(
            CatalogProductsByArticlesRequest(articles = listOf(article))
        )

        return products.firstOrNull()
            ?: throw IllegalArgumentException("Товар с артикулом $article не найден")
    }

    private fun validateCatalogProductAvailability(
        product: CatalogProductDto,
        requestedQuantity: Int
    ) {
        require(product.isActive) {
            "Товар с артикулом ${product.article} недоступен"
        }

        require(requestedQuantity <= product.stockQuantity) {
            "Недостаточно товара ${product.article} на складе. " +
                    "Доступно: ${product.stockQuantity}, запрошено: $requestedQuantity"
        }
    }
}