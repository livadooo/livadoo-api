package com.livadoo.services.customer.services

import com.livadoo.services.customer.data.CustomerDto

interface CustomerService {

    suspend fun createCustomer(userId: String, createdBy: String)

    suspend fun getCustomer(customerId: String): CustomerDto
}
