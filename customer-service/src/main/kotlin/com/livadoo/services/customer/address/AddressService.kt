package com.livadoo.services.customer.address

import com.livadoo.services.customer.address.data.Address
import com.livadoo.services.customer.address.data.AddressCreate
import com.livadoo.services.customer.address.data.AddressEdit

interface AddressService {

    suspend fun createAddress(addressCreate: AddressCreate)

    suspend fun updateAddress(addressEdit: AddressEdit)

    suspend fun getAddress(addressId: String): Address

    suspend fun deleteAddress(addressId: String)

    suspend fun getAddresses(): List<Address>
}