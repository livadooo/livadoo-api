package com.livadoo.services.user.services

import com.livadoo.proxy.user.UserServiceProxy
import com.livadoo.utils.user.UserDto
import org.springframework.stereotype.Service

@Service
class DefaultUserServiceProxy(
    private val userService: UserService,
) : UserServiceProxy {
    override suspend fun getUserById(userId: String): UserDto {
        return userService.getUserById(userId)
    }

    override suspend fun getUserByEmail(email: String): UserDto {
        return userService.getUserByEmail(email)
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserDto {
        return userService.getUserByPhoneNumber(phoneNumber)
    }
}
