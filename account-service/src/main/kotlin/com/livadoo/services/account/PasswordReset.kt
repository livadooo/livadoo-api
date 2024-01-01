package com.livadoo.services.account

data class PasswordReset(val resetKey: String, val password: String)
