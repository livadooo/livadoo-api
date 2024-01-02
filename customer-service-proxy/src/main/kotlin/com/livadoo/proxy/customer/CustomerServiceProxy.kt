package com.livadoo.proxy.customer

interface CustomerServiceProxy {
    suspend fun createCustomer(userId: String, createdBy: String)
}
