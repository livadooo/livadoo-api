package com.livadoo.services.customer.controller

import com.livadoo.services.customer.data.CustomerCreate
import com.livadoo.services.customer.services.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/customers")
class CustomerController @Autowired constructor(
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