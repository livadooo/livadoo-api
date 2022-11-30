package com.livadoo.services.customer.services

import com.livadoo.services.customer.data.Customer
import com.livadoo.services.customer.data.CustomerCreate

interface CustomerService {

    suspend fun createCustomer(customerCreate: CustomerCreate)

    suspend fun updateCustomer(customerCreate: CustomerCreate)

    suspend fun getCustomer(customerId: String): Customer
}