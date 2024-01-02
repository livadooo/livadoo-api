package com.livadoo.services.customer.services.mongodb

import com.livadoo.services.customer.buildCustomerId
import com.livadoo.services.customer.data.CustomerDto
import com.livadoo.services.customer.exceptions.CustomerNotFoundException
import com.livadoo.services.customer.exceptions.DuplicateCustomerUser
import com.livadoo.services.customer.services.CustomerService
import com.livadoo.services.customer.services.mongodb.entity.CustomerEntity
import com.livadoo.services.customer.services.mongodb.entity.toDto
import com.livadoo.services.customer.services.mongodb.repository.CustomerRepository
import com.livadoo.shared.extension.containsExceptionKey
import com.livadoo.utils.exception.InternalErrorException
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class MongoCustomerService(
    private val customerRepository: CustomerRepository,
    private val clock: Clock,
) : CustomerService {
    override suspend fun createCustomer(userId: String, createdBy: String) {
        val customerEntity = CustomerEntity(
            userId = userId,
            customerId = buildCustomerId,
            createdBy = createdBy,
            createdAt = clock.instant(),
        )
        saveCustomer(customerEntity)
    }

    override suspend fun getCustomer(customerId: String): CustomerDto {
        return customerRepository.findByCustomerId(customerId)
            ?.toDto()
            ?: throw CustomerNotFoundException(customerId)
    }

    private suspend fun saveCustomer(customerEntity: CustomerEntity): CustomerEntity {
        return try {
            customerRepository.save(customerEntity)
        } catch (exception: DuplicateKeyException) {
            if (exception.message!!.containsExceptionKey("customerId")) {
                saveCustomer(customerEntity.copy(customerId = buildCustomerId))
            } else if (exception.message!!.containsExceptionKey("userId")) {
                throw DuplicateCustomerUser(customerEntity.userId)
            } else {
                throw InternalErrorException()
            }
        }
    }
}
