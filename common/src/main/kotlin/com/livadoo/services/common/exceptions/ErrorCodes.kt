package com.livadoo.services.common.exceptions

object ErrorCodes {
    /* 400xxx [BAD INPUT] */
    const val WRONG_PASSWORD = "400001"
    const val SIMILAR_PASSWORDS = "400002"
    const val SECURE_KEY_EXPIRED = "400003"
    const val AUTHORITY_NOT_FOUND = "400004"
    const val USER_EMAIL_IN_USE = "400005"
    const val USER_PHONE_IN_USE = "400006"

    /* 401xxx [UNAUTHORIZED] */
    const val WRONG_CREDENTIALS = "401001"
    const val NOT_AUTHENTICATED = "401002"
    const val ACCOUNT_NOT_VERIFIED = "401003"
    const val ACCOUNT_BLOCKED = "401004"
    const val ACCOUNT_DELETED = "401005"

    /* 403xxx [FORBIDDEN] */
    const val FORBIDDEN = "403001"

    /* 404xxx [NOT FOUND] */
    const val USER_NOT_FOUND = "404001"
    const val CUSTOMER_NOT_FOUND = "404002"
    const val FILE_NOT_FOUND = "404003"
    const val DOCUMENT_NOT_FOUND = "404004"
    const val ADDRESS_NOT_FOUND = "404005"
    const val CATEGORY_NOT_FOUND = "404006"
    const val SECURE_KEY_NOT_FOUND = "404007"
    const val PRODUCT_NOT_FOUND = "404008"
}