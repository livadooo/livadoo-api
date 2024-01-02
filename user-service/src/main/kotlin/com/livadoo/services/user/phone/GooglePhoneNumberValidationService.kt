package com.livadoo.services.user.phone

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import com.livadoo.services.user.exceptions.InvalidPhoneNumberException
import org.springframework.stereotype.Service

@Service
class GooglePhoneNumberValidationService(
    private val phoneNumberUtil: PhoneNumberUtil,
) : PhoneNumberValidationService {
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
