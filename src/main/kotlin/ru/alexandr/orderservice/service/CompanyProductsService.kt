package ru.alexandr.orderservice.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.alexandr.orderservice.config.security.SecurityUtils.getCompanyId
import ru.alexandr.orderservice.controller.ProductCreateRequest
import ru.alexandr.orderservice.entity.PriceTier
import ru.alexandr.orderservice.entity.Product
import ru.alexandr.orderservice.repository.PriceTierRepository
import ru.alexandr.orderservice.repository.ProductRepository
import java.time.LocalDateTime

@Service
class CompanyProductsService(
    private val productRepository: ProductRepository,
    private val priceTierRepository: PriceTierRepository,
) {
    @Transactional
    fun createProduct(req: ProductCreateRequest) {
        val companyId = getCompanyId()
        require(companyId != null) { "CompanyId must not be null" }
        val product = Product(
            name = req.name,
            description = req.description,
            categoryId = req.categoryId,
            companyId = companyId,
            availableQuantity = req.availableQuantity,
            unit = req.unit,
            priceRetail = req.priceRetail,
            updatedAt = LocalDateTime.now()
        )

        val savedProduct = productRepository.save(product)

        if (req.priceTiers.isNotEmpty()) {
            val tiers = req.priceTiers.map {
                PriceTier(
                    productId = savedProduct.id,
                    quantityFrom = it.quantityFrom,
                    pricePerUnit = it.pricePerUnit
                )
            }

            priceTierRepository.saveAll(tiers)
        }


    }
}