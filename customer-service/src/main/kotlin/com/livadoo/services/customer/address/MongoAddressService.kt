package com.livadoo.services.customer.address

import com.livadoo.services.customer.address.data.Address
import com.livadoo.services.customer.address.data.AddressCreate
import com.livadoo.services.customer.address.data.AddressEdit
import com.livadoo.services.customer.exceptions.AddressNotFoundException
import com.livadoo.services.customer.services.mongodb.repository.CustomerRepository
import com.livadoo.utils.exception.InternalErrorException
import com.livadoo.utils.security.config.AppSecurityContext
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MongoAddressService(
    private val addressRepository: AddressRepository,
    private val customerRepository: CustomerRepository,
    private val securityContext: AppSecurityContext,
) : AddressService {
    override suspend fun createAddress(addressCreate: AddressCreate) {
        val (countryCode, fullName, phoneNumber, address, city, region, isDefault) = addressCreate
        val customerEntity = customerRepository.findByUserId(securityContext.getCurrentUserId())
            ?: throw InternalErrorException()

        val addressEntity = AddressEntity(
            customerId = customerEntity.customerId,
            countryCode = countryCode,
            fullName = fullName,
            phoneNumber = phoneNumber,
            address = address,
            city = city,
            region = region,
            isDefault = isDefault,
        )

        addressRepository.save(addressEntity).awaitSingle()
    }

    override suspend fun updateAddress(addressEdit: AddressEdit) {
        val addressId = addressEdit.addressId
        val addressEntity = addressRepository.findById(addressId).awaitSingleOrNull()
            ?: throw AddressNotFoundException(addressId)

        addressEntity.apply {
            countryCode = addressEdit.countryCode
            fullName = addressEdit.fullName
            phoneNumber = addressEdit.phoneNumber
            address = addressEdit.address
            city = addressEdit.city
            region = addressEdit.region
            isDefault = addressEdit.isDefault
            updatedAt = Instant.now()
        }
        addressRepository.save(addressEntity).awaitSingle()
    }

    override suspend fun getAddress(addressId: String): Address {
        return addressRepository.findById(addressId).awaitSingleOrNull()
            ?.toDto()
            ?: throw AddressNotFoundException(addressId)
    }

    override suspend fun deleteAddress(addressId: String) {
        addressRepository.deleteById(addressId).awaitSingleOrNull()
    }

    override suspend fun getAddresses(): List<Address> {
        val customerEntity = customerRepository.findByUserId(securityContext.getCurrentUserId())
            ?: throw InternalErrorException()
        return addressRepository.findByCustomerId(customerEntity.customerId)
            .map { it.toDto() }
            .asFlow()
            .toList()
    }
}
