package com.livadoo.services.customer.proxy

import com.livadoo.proxy.customer.CustomerServiceProxy
import com.livadoo.proxy.customer.model.CustomerCreate
import com.livadoo.services.customer.data.CustomerCreate as InternalCustomerCreate
import com.livadoo.services.customer.services.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerServiceProxyImpl @Autowired constructor(
    private val customerService: CustomerService
) : CustomerServiceProxy {

    override suspend fun createCustomer(customerCreate: CustomerCreate) {
        customerService.createCustomer(customerCreate.toInternalCustomerCreate())
    }

    fun CustomerCreate.toInternalCustomerCreate() = InternalCustomerCreate(userId)
}