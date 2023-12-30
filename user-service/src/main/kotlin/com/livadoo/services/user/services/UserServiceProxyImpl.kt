package com.livadoo.services.user.services

import com.livadoo.proxy.user.UserServiceProxy
import com.livadoo.proxy.user.model.User
import org.springframework.stereotype.Service
import com.livadoo.services.user.data.User as InternalUser

@Service
class UserServiceProxyImpl(
    private val userService: UserService
) : UserServiceProxy {

    override suspend fun getUser(userId: String): User {
        return userService.getUser(userId).toUser()
    }

    override suspend fun getUsersByIds(userIds: List<String>): List<User> {
        return userService.getUsersByIds(userIds)
            .map { it.toUser() }
            .toList()
    }
}

fun InternalUser.toUser(): User = User(
    firstName,
    lastName,
    phoneNumber,
    email,
    city,
    country,
    address,
    authority,
    portraitUrl,
    userId!!
)