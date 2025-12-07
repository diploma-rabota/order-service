package ru.alexandr.orderservice.service

import org.springframework.stereotype.Service
import ru.alexandr.orderservice.config.security.SecurityUtils.getCompanyId
import ru.alexandr.orderservice.controller.ProductCreateRequest
import ru.alexandr.orderservice.entity.Product
import ru.alexandr.orderservice.repository.ProductRepository
import java.time.LocalDateTime

@Service
class CompanyProductsService(
    private val productRepository: ProductRepository
) {

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
            priceWholesale = req.priceWholesale,
            minQuantityWholesale = req.minQuantityWholesale,
            updatedAt = LocalDateTime.now()
        )

        productRepository.save(product)

    }
}