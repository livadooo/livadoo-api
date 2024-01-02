package com.livadoo.services.user

object ErrorCodes {
    /* 409xxx Codes */
    const val USER_PHONE_NUMBER_IN_USE = "4090100"
    const val USER_EMAIL_IN_USE = "4090101"

    /* 400xxx Codes */
    const val INVALID_PHONE_NUMBER = "4000100"
    const val INVALID_STAFF_ROLES = "4000101"
}
