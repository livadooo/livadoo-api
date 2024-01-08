package com.livadoo.services.role

import com.livadoo.shared.extension.generateId

val buildRoleId: String
    get() = generateId(prefix = "ROLE-", length = 5)
