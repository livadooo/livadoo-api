package com.livadoo.services.user.services

import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffCreate
import com.livadoo.services.user.data.User
import com.livadoo.services.user.data.UserCreate
import com.livadoo.services.user.data.UserUpdate
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart


interface UserService {

    suspend fun createStaffUser(staffCreate: StaffCreate)

    suspend fun createCustomerUser(userCreate: UserCreate)

    suspend fun updateUser(userUpdate: UserUpdate): User

    suspend fun verifyAccount(key: String): User

    suspend fun getAdminUsers(pageable: Pageable): Pair<List<User>, Long>

    suspend fun getCustomerUsers(pageable: Pageable): Pair<List<User>, Long>

    suspend fun getUsersByIds(userIds: List<String>): List<User>

    suspend fun getUser(userId: String): User

    suspend fun blockUser(userId: String)

    suspend fun deleteUser(userId: String)

    suspend fun getAuthorities(): List<String>

    suspend fun changePassword(userId: String, passwordUpdate: PasswordUpdate)

    suspend fun updateUserAvatar(filePart: FilePart, userId: String)
}
