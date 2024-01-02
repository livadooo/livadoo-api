package com.livadoo.services.customer

import com.livadoo.shared.extension.generateId

val buildCustomerId: String
    get() = generateId(prefix = "CUS-", length = 5)
