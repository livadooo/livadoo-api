package com.livadoo.services.phone.validation

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class PhoneNumberConfig {
    @Bean
    fun providePhoneNumberUtil(): PhoneNumberUtil {
        return PhoneNumberUtil.getInstance()
    }
}
