package com.livadoo.services.phone.validation

import com.livadoo.proxy.phone.validation.PhoneValidationServiceProxy
import org.springframework.stereotype.Service

@Service
class DefaultPhoneValidationServiceProxy(
    private val phoneValidationService: PhoneValidationService,
) : PhoneValidationServiceProxy {
    override fun validate(phoneNumber: String, regionCode: String): String {
        return phoneValidationService.validate(phoneNumber = phoneNumber, regionCode = regionCode)
    }
}
