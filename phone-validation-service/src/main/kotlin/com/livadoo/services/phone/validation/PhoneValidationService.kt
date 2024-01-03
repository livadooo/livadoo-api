package com.livadoo.services.phone.validation

interface PhoneValidationService {
    fun validate(phoneNumber: String, regionCode: String): String
}
