package ru.alexandr.orderservice.client


import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "userClient",
    url = "\${clients.user-service.url}"
)
interface UserClient {

    @GetMapping("/internal/users/{userId}/email")
    fun getUserEmail(@PathVariable userId: Long): UserEmailResponse

}


data class UserEmailResponse(
    val userId: Long,
    val email: String
)