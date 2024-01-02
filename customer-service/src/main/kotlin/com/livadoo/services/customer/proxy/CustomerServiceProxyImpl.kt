package com.livadoo.services.customer.proxy

import com.livadoo.proxy.customer.CustomerServiceProxy
import com.livadoo.services.customer.services.CustomerService
import org.springframework.stereotype.Service

@Service
class CustomerServiceProxyImpl(
    private val customerService: CustomerService,
) : CustomerServiceProxy {
    override suspend fun createCustomer(userId: String, createdBy: String) {
        customerService.createCustomer(userId = userId, createdBy = createdBy)
    }
}
