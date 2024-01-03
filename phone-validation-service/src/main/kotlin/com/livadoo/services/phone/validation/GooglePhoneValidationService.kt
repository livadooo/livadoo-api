package com.livadoo.services.phone.validation

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import org.springframework.stereotype.Service

@Service
class GooglePhoneValidationService(
    private val phoneNumberUtil: PhoneNumberUtil,
) : PhoneValidationService {
    override fun validate(phoneNumber: String, regionCode: String): String {
        return try {
            val parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, regionCode)
            if (phoneNumberUtil.isValidNumber(parsedPhoneNumber)) {
                phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberFormat.E164)
            } else {
                throw InvalidPhoneNumberException(phoneNumber)
            }
        } catch (exception: NumberParseException) {
            throw InvalidPhoneNumberException(phoneNumber)
        }
    }
}
