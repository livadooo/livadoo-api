package com.livadoo.services.customer.controller

import com.livadoo.services.customer.address.AddressService
import com.livadoo.services.customer.address.data.AddressCreate
import com.livadoo.services.customer.address.data.AddressEdit
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/customers/addresses")
class AddressController(
    private val addressService: AddressService
) {

    @PostMapping
    suspend fun createAddress(@RequestBody addressCreate: AddressCreate) {
        addressService.createAddress(addressCreate)
    }

    @PutMapping
    suspend fun updateAddress(@RequestBody addressEdit: AddressEdit) {
        addressService.updateAddress(addressEdit)
    }
}