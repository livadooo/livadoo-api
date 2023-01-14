package com.livadoo.proxy.user

import com.livadoo.proxy.user.model.User

interface UserServiceProxy {

	suspend fun getUser(userId: String): User

	suspend fun getUsersByIds(userIds: List<String>): List<User>
}