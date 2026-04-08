package ru.alexandr.orderservice.client


import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "userClient",
    url = "\${clients.user-service.url}"
)
interface UserClient {

    @GetMapping("/api/users/{id}")
    fun getUserById(
        @PathVariable id: Long
    ): UserDto
}


data class UserDto(
    val id: Long,
    val email: String,
    val userName: String
)