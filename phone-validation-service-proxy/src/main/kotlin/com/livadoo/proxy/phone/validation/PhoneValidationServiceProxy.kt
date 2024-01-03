package com.livadoo.proxy.phone.validation

interface PhoneValidationServiceProxy {
    fun validate(phoneNumber: String, regionCode: String): String
}
