package com.livadoo.services.user

import com.livadoo.services.user.data.CustomerUserCreate
import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffUserCreate
import com.livadoo.services.user.data.UserUpdate
import com.livadoo.services.user.services.UserService
import com.livadoo.utils.user.UserDto
import jakarta.validation.Valid
import org.springframework.http.codec.multipart.FilePart
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/staff")
    suspend fun createStaffUser(
        @RequestBody @Valid user: StaffUserCreate,
    ) {
        return userService.createStaffUser(user)
    }

    @PostMapping("/customers")
    suspend fun createCustomerUser(
        @RequestBody @Valid customerUserCreate: CustomerUserCreate,
    ) {
        return userService.createCustomerUser(customerUserCreate)
    }

    @PutMapping
    suspend fun updateUser(
        @RequestBody @Validated user: UserUpdate,
    ): UserDto {
        return userService.updateUser(user)
    }

    @GetMapping("/{userId}")
    suspend fun getUser(
        @PathVariable userId: String,
    ): UserDto {
        return userService.getUserById(userId)
    }

    @PutMapping("/block/{userId}")
    suspend fun blockUser(
        @PathVariable userId: String,
    ) {
        return userService.blockUser(userId)
    }

    @DeleteMapping("/{userId}")
    suspend fun deleteUser(
        @PathVariable userId: String,
    ) {
        return userService.deleteUser(userId)
    }

    @PutMapping("/{userId}/update-password")
    suspend fun updatePassword(
        @PathVariable userId: String,
        @RequestBody passwordUpdate: PasswordUpdate,
    ) {
        return userService.changePassword(userId, passwordUpdate)
    }

    @PutMapping("/{userId}/portrait")
    suspend fun updateUserPortrait(
        @RequestPart("file") filePart: FilePart,
        @PathVariable userId: String,
    ): String {
        return userService.updateUserPortrait(filePart, userId)
    }

    @DeleteMapping("/{userId}/portrait")
    suspend fun updateUserPortrait(
        @PathVariable userId: String,
    ) {
        return userService.deleteUserPortrait(userId)
    }
}
