package com.livadoo.services.customer.controller

import com.livadoo.services.customer.address.AddressService
import com.livadoo.services.customer.address.data.AddressCreate
import com.livadoo.services.customer.address.data.AddressEdit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/customers/addresses")
class AddressController @Autowired constructor(
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