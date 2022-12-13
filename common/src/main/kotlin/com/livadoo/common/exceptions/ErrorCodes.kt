package com.livadoo.common.exceptions

object ErrorCodes {
    /* 404xxx [NOT FOUND] */
    const val USER_WITH_ID_NOT_FOUND = "404010"
    const val USER_WITH_EMAIL_NOT_FOUND = "404011"
    const val USER_WITH_PHONE_NOT_FOUND = "404012"
    const val CUSTOMER_NOT_FOUND = "404013"
    const val FILE_NOT_FOUND = "404014"
    const val DOCUMENT_NOT_FOUND = "404015"
    const val ADDRESS_NOT_FOUND = "404016"
    const val CATEGORY_NOT_FOUND = "404017"
    const val SECURE_KEY_NOT_FOUND = "404018"
    const val PRODUCT_NOT_FOUND = "404019"

    /* 409xxx [CONFLICT] */
    const val USER_EMAIL_IN_USE = "409010"
    const val USER_PHONE_IN_USE = "409011"

    /* 400xxx [BAD INPUT] */
    const val WRONG_PASSWORD = "400010"
    const val SIMILAR_PASSWORDS = "400011"
    const val SECURE_KEY_EXPIRED = "400012"
    const val AUTHORITY_INVALID = "400013"

    /* 401xxx [UNAUTHORIZED] */
    const val UNAUTHORIZED = "401010"

    /* 403xxx [FORBIDDEN] */
    const val FORBIDDEN = "403010"
}