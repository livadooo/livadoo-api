package com.livadoo.services.permission

data class PermissionCreate(
    val roleId: String,
    val permission: String,
    val description: String,
)
