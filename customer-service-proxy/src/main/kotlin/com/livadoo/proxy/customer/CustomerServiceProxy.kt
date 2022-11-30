package com.livadoo.proxy.customer

import com.livadoo.proxy.customer.model.CustomerCreate

interface CustomerServiceProxy {
    suspend fun createCustomer(customerCreate: CustomerCreate)
}