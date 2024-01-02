package com.livadoo.proxy.user

import com.livadoo.utils.user.UserDto

interface UserServiceProxy {
    suspend fun getUserById(userId: String): UserDto

    suspend fun getUsersByIds(userIds: List<String>): List<UserDto>
}
