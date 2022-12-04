package com.livadoo.services.user.controller

import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffUserCreate
import com.livadoo.services.user.data.User
import com.livadoo.services.user.data.CustomerUserCreate
import com.livadoo.services.user.data.UserUpdate
import com.livadoo.services.user.security.AuthUserDTO
import com.livadoo.services.user.services.AccountService
import com.livadoo.services.user.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val accountService: AccountService
) {

    @PostMapping("/staff")
    suspend fun createStaffUser(@RequestBody @Validated user: StaffUserCreate) {
        return userService.createStaffUser(user)
    }

    @PostMapping("/customer")
    suspend fun createCustomerUser(@RequestBody customerUserCreate: CustomerUserCreate) {
        return userService.createCustomerUser(customerUserCreate)
    }

    @PutMapping
    suspend fun updateUser(@RequestBody @Validated user: UserUpdate): User {
        return userService.updateUser(user)
    }

    @GetMapping("/verify")
    suspend fun verifyAccount(@RequestParam("key") key: String): AuthUserDTO {
        val user = userService.verifyAccount(key)
        return accountService.loginAfterAccountActivation(user.email)
    }

    @GetMapping("/admins")
    suspend fun getAdminUsers(
        @RequestParam("page", required = false) page: Int?,
        @RequestParam("size", required = false) size: Int?
    ): ResponseEntity<List<User>> {
        val (adminUsers, adminCount) = userService.getAdminUsers(PageRequest.of(page ?: 0, size ?: 100))
        return ResponseEntity.ok()
            .header("X-Total-Count", "$adminCount")
            .body(adminUsers)
    }

    @GetMapping("/customers")
    suspend fun getCustomerUsers(
        @RequestParam("page", required = false) page: Int?,
        @RequestParam("size", required = false) size: Int?
    ): ResponseEntity<List<User>> {
        val (customerUsers, customersCount) = userService.getCustomerUsers(PageRequest.of(page ?: 0, size ?: 100))
        return ResponseEntity.ok()
            .header("X-Total-Count", "$customersCount")
            .body(customerUsers)
    }

    @GetMapping("/byUserIds")
    suspend fun getUsersByIds(@RequestParam("userIds") userIdsInString: String): List<User> {
        val userIds = userIdsInString.split(USER_IDS_SEPARATOR)
        return userService.getUsersByIds(userIds)
    }

    @GetMapping("/{userId}")
    suspend fun getUser(@PathVariable userId: String): User {
        return userService.getUser(userId)
    }

    @PutMapping("/block/{userId}")
    suspend fun blockUser(@PathVariable userId: String) {
        return userService.blockUser(userId)
    }

    @DeleteMapping("/{userId}")
    suspend fun deleteUser(@PathVariable userId: String) {
        return userService.deleteUser(userId)
    }

    @GetMapping("/authorities")
    suspend fun getAuthorities(): List<String> {
        return userService.getAuthorities()
    }

    @PutMapping("/{userId}/update-password")
    suspend fun updatePassword(
        @PathVariable userId: String,
        @RequestBody passwordUpdate: PasswordUpdate
    ) {
        return userService.changePassword(userId, passwordUpdate)
    }

    @PutMapping("/avatar/{userId}")
    suspend fun updateUserAvatar(
        @RequestPart("avatar") filePart: FilePart,
        @PathVariable userId: String
    ) {
        return userService.updateUserAvatar(filePart, userId)
    }

    companion object {
        private const val USER_IDS_SEPARATOR: String = ","
    }
}
