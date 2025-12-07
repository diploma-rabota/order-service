package ru.alexandr.orderservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alexandr.orderservice.service.CompanyProductsService
import ru.alexandr.orderservice.util.jwt.UnitType

@RestController
@RequestMapping("/company")
class CompanyController(
    private val companyProductsService: CompanyProductsService
) {

    @PostMapping("/create-product")
    fun createProductForCompany(@RequestBody request: ProductCreateRequest){
        companyProductsService.createProduct(request)
    }

}

data class ProductCreateRequest(
    val name: String,
    val description: String,
    val categoryId: Long,
    val availableQuantity: Long,
    val unit: UnitType,
    val priceRetail: Long,
    val priceWholesale: Long,
    val minQuantityWholesale: Long
)