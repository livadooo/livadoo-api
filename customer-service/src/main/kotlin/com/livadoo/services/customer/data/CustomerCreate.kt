package com.livadoo.services.customer.data

import org.springframework.util.Base64Utils
import java.time.LocalDate

data class CustomerCreate(
    val userId: String
)

fun CustomerCreate.buildCustomerId(): String {
    val string = "${this.userId}${LocalDate.now().toEpochDay()}"
    return Base64Utils.encodeToString(string.toByteArray())
}