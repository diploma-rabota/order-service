package ru.alexandr.orderservice.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.alexandr.orderservice.dto.cart.CatalogProductDto
import ru.alexandr.orderservice.dto.cart.CatalogProductsByArticlesRequest


@FeignClient(
    name = "catalogClient",
    url = "\${clients.catalog-service.url}"
)
interface CatalogClient {

    @PostMapping("/api/products/by-articles")
    fun getProductsByArticles(
        @RequestBody request: CatalogProductsByArticlesRequest
    ): List<CatalogProductDto>
}