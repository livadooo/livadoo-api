package com.livadoo.services.permission

data class PermissionCreate(
    val roleId: String,
    val isBase: Boolean,
    val permission: String,
    val description: String,
)
