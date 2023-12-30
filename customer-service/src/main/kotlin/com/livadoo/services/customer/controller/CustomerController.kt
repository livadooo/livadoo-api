package com.livadoo.services.customer.controller

import com.livadoo.services.customer.data.CustomerCreate
import com.livadoo.services.customer.services.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping
    suspend fun createCustomer(@RequestBody customerCreate: CustomerCreate) {
        customerService.createCustomer(customerCreate)
    }

    @PutMapping
    suspend fun updateCustomer(@RequestBody customerCreate: CustomerCreate) {
        customerService.updateCustomer(customerCreate)
    }
}