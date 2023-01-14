package com.livadoo.services.customer.address

import com.livadoo.common.exceptions.NotAuthenticatedException
import com.livadoo.library.security.utils.currentUserId
import com.livadoo.services.customer.address.data.Address
import com.livadoo.services.customer.address.data.AddressCreate
import com.livadoo.services.customer.address.data.AddressEdit
import com.livadoo.services.customer.exceptions.AddressNotFoundException
import com.livadoo.services.customer.services.mongodb.repository.CustomerRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MongoAddressService @Autowired constructor(
    private val addressRepository: AddressRepository,
    private val customerRepository: CustomerRepository
) : AddressService {

    override suspend fun createAddress(addressCreate: AddressCreate) {
        val (countryCode, fullName, phoneNumber, address, city, region, isDefault) = addressCreate
        val customerId = currentUserId.awaitSingleOrNull()
            ?.let { customerRepository.findByUserId(it).awaitSingle() }
            ?.customerId
            ?: throw NotAuthenticatedException()

        val addressEntity = AddressEntity(customerId, countryCode, fullName, phoneNumber, address, city, region, isDefault)

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
        return currentUserId.awaitFirstOrNull()
            ?.let { customerRepository.findByUserId(it).awaitSingle().customerId }
            ?.let { customerId -> addressRepository.findByCustomerId(customerId).map { it.toDto() }.asFlow().toList() }
            ?: throw NotAuthenticatedException()
    }
}