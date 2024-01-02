package com.livadoo.services.customer.controller

import com.livadoo.services.customer.services.CustomerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/customers")
class CustomerController(
    private val customerService: CustomerService,
) {
    @GetMapping("/{customerId}")
    suspend fun getCustomer(@PathVariable customerId: String) {
        customerService.getCustomer(customerId)
    }
}
