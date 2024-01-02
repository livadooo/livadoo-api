package com.livadoo.services.user.phone

interface PhoneNumberValidationService {
    fun validate(phoneNumber: String, regionCode: String): String
}
