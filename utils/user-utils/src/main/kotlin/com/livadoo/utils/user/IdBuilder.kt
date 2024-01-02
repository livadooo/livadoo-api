package com.livadoo.utils.user

import com.livadoo.shared.extension.generateId

val buildUserId: String
    get() = generateId(prefix = "U-", length = 10)
