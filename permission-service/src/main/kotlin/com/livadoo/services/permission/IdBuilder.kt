package com.livadoo.services.permission

import com.livadoo.shared.extension.generateId

val buildPermissionId: String
    get() = generateId(prefix = "PERM-", length = 5)
