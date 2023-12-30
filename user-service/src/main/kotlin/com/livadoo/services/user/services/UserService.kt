package com.livadoo.services.user.services

import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffUserCreate
import com.livadoo.services.user.data.User
import com.livadoo.services.user.data.CustomerUserCreate
import com.livadoo.services.user.data.UserUpdate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.codec.multipart.FilePart


interface UserService {

    suspend fun createStaffUser(staffUserCreate: StaffUserCreate)

    suspend fun createCustomerUser(customerUserCreate: CustomerUserCreate)

    suspend fun updateUser(userUpdate: UserUpdate): User

    suspend fun verifyAccount(key: String): User

    suspend fun getStaffUsers(pageRequest: PageRequest, query: String): Page<User>

    suspend fun getCustomerUsers(pageRequest: PageRequest, query: String): Page<User>

    suspend fun getUsersByIds(userIds: List<String>): List<User>

    suspend fun getUser(userId: String): User

    suspend fun blockUser(userId: String)

    suspend fun deleteUser(userId: String)

    suspend fun getAuthorities(): List<String>

    suspend fun changePassword(userId: String, passwordUpdate: PasswordUpdate)

    suspend fun updateUserPortrait(filePart: FilePart, userId: String): String

    suspend fun deleteUserPortrait(userId: String)
}
