package com.livadoo.services.customer.services.mongodb

import com.livadoo.library.security.domain.SYSTEM_ACCOUNT
import com.livadoo.services.customer.data.Customer
import com.livadoo.services.customer.data.CustomerCreate
import com.livadoo.services.customer.data.buildCustomerId
import com.livadoo.services.customer.exceptions.CustomerNotFoundException
import com.livadoo.services.customer.services.CustomerService
import com.livadoo.services.customer.services.mongodb.entity.CustomerEntity
import com.livadoo.services.customer.services.mongodb.entity.toDto
import com.livadoo.services.customer.services.mongodb.repository.CustomerRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MongoCustomerService(
    private val customerRepository: CustomerRepository,
) : CustomerService {
    private val logger = LoggerFactory.getLogger(MongoCustomerService::class.java)

    override suspend fun createCustomer(customerCreate: CustomerCreate) {
        val customerId = customerCreate.buildCustomerId()
        val entity = CustomerEntity(customerCreate.userId, customerId, createdBy = SYSTEM_ACCOUNT)
        val customer = customerRepository.save(entity).map { it.toDto() }.awaitSingle()
        logger.debug("Customer created with id: ${customer.customerId}")
    }

    override suspend fun updateCustomer(customerCreate: CustomerCreate) {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomer(customerId: String): Customer {
        return customerRepository.findByCustomerId(customerId).awaitSingleOrNull()
            ?.toDto()
            ?: throw CustomerNotFoundException(customerId)
    }
}
