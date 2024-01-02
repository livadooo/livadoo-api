package com.livadoo.services.user.services

import com.livadoo.services.user.data.CustomerUserCreate
import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffUserCreate
import com.livadoo.services.user.data.UserUpdate
import com.livadoo.utils.user.UserDto
import org.springframework.http.codec.multipart.FilePart

interface UserService {
    suspend fun createStaffUser(staffUserCreate: StaffUserCreate): UserDto

    suspend fun createCustomerUser(customerUserCreate: CustomerUserCreate): UserDto

    suspend fun updateUser(userUpdate: UserUpdate): UserDto

    suspend fun getUserById(userId: String): UserDto

    suspend fun blockUser(userId: String)

    suspend fun deleteUser(userId: String)

    suspend fun updateStaffPassword(userId: String, passwordUpdate: PasswordUpdate)

    suspend fun updateUserPortrait(filePart: FilePart, userId: String): String

    suspend fun deleteUserPortrait(userId: String)
}
