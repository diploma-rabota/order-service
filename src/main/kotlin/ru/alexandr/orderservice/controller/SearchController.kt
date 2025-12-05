package ru.alexandr.orderservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController {



//    @PostMapping("/by-company")
//    fun searchByCompany(@RequestBody request: SearchRequest): JwtResponse {
//        return registrationService.register(request)
//    }
}


data class SearchRequest(val query: String)