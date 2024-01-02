package com.livadoo.services.user.services

import com.livadoo.proxy.customer.CustomerServiceProxy
import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.user.PasswordResetProperties
import com.livadoo.services.user.data.CustomerUserCreate
import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffUserCreate
import com.livadoo.services.user.data.UserUpdate
import com.livadoo.services.user.exceptions.NotAllowedToEditUserException
import com.livadoo.services.user.exceptions.SimilarPasswordException
import com.livadoo.services.user.exceptions.UserEmailTakenException
import com.livadoo.services.user.exceptions.UserNotFoundException
import com.livadoo.services.user.exceptions.UserPhoneTakenException
import com.livadoo.services.user.exceptions.WrongPasswordException
import com.livadoo.utils.security.config.AppSecurityContext
import com.livadoo.utils.security.domain.SYSTEM_ACCOUNT
import com.livadoo.utils.spring.extractContent
import com.livadoo.utils.user.UserDto
import com.livadoo.utils.user.toDto
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Base64
import java.util.UUID

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
    private val notificationService: NotificationServiceProxy,
    private val passwordEncoder: PasswordEncoder,
    private val passwordResetProperties: PasswordResetProperties,
    private val storageService: StorageServiceProxy,
    private val customerService: CustomerServiceProxy,
    private val securityContext: AppSecurityContext,
) : UserService {
    private val logger = LoggerFactory.getLogger(DefaultUserService::class.java)

    override suspend fun createCustomerUser(customerUserCreate: CustomerUserCreate) {
        TODO("Not yet implemented")
    }

    override suspend fun createStaffUser(staffUserCreate: StaffUserCreate) {
        TODO("Not yet implemented")
    }

    private suspend fun checkEmailAndPassword(
        email: String,
        phoneNumber: String,
    ) {
        userRepository
            .findByEmailIgnoreCase(email).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) {
                    userRepository.delete(user).awaitSingleOrNull()
                } else {
                    throw UserEmailTakenException(email)
                }
            }

        userRepository
            .findByPhoneNumber(phoneNumber).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) {
                    userRepository.delete(user).awaitSingleOrNull()
                } else {
                    throw UserPhoneTakenException(phoneNumber)
                }
            }
    }

    override suspend fun verifyAccount(key: String): UserDto {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(userUpdate: UserUpdate): UserDto {
        logger.debug("Updating user with userId: ${userUpdate.userId}")

        val isGranted =
            with(securityContext) {
                getCurrentUserId() == userUpdate.userId || currentUserHasPermissions(emptyList())
            }
        if (!isGranted) throw NotAllowedToEditUserException()

        val entity =
            userRepository.findById(userUpdate.userId).awaitSingleOrNull()
                ?: throw UserNotFoundException(userUpdate.userId)

        userRepository.findByPhoneNumber(userUpdate.phoneNumber).awaitSingleOrNull()
            ?.also { existingUserWithPhone ->
                if (existingUserWithPhone.id != userUpdate.userId) throw UserPhoneTakenException(userUpdate.phoneNumber)
            }

        entity.apply {
            firstName = userUpdate.firstName
            lastName = userUpdate.lastName
            phoneNumber = userUpdate.phoneNumber
            address = userUpdate.address
            city = userUpdate.city
            country = userUpdate.country
            updatedAt = Instant.now()
            updatedBy = userUpdate.updatedBy
        }

        return userRepository.save(entity).awaitSingle()
            .toDto()
            .also { logger.debug("UserDto with userId: ${it.userId} was successfully updated") }
    }

    override suspend fun updateUserPortrait(
        filePart: FilePart,
        userId: String,
    ): String {
        val userEntity =
            userRepository.findById(userId).awaitSingleOrNull()
                ?: throw UserNotFoundException(userId)

        userEntity.photoUrl = uploadPortrait(filePart)
        return userRepository.save(userEntity).awaitSingle().photoUrl!!
    }

    override suspend fun deleteUserPortrait(userId: String) {
        val userEntity =
            userRepository.findById(userId).awaitSingleOrNull()
                ?: throw UserNotFoundException(userId)

        userEntity.photoUrl = null
        userRepository.save(userEntity).awaitSingle()
    }

    override suspend fun getUsersByIds(userIds: List<String>): List<UserDto> {
        return userRepository.findAllByIdIn(userIds)
            .map { it.toDto() }
            .asFlow()
            .toList()
    }

    override suspend fun getUserById(userId: String): UserDto {
        return userRepository.findById(userId).map { it.toDto() }.awaitFirstOrNull()
            ?: throw UserNotFoundException(userId = userId)
    }

    override suspend fun blockUser(userId: String) {
        logger.debug("Blocking user with userId: $userId")
        val entity =
            userRepository.findById(userId).awaitFirstOrNull()
                ?: throw UserNotFoundException(userId)

        entity.blocked = true
        entity.updatedAt = Instant.now()
        entity.updatedBy = SYSTEM_ACCOUNT
        userRepository.save(entity).awaitSingle()
        logger.debug("UserDto with userId: $userId blocked")
    }

    override suspend fun deleteUser(userId: String) {
        logger.debug("Deleting user with userId: $userId")
        val entity =
            userRepository.findById(userId).awaitFirstOrNull()
                ?: throw UserNotFoundException(userId)

        entity.deleted = true
        entity.updatedAt = Instant.now()
        entity.updatedBy = SYSTEM_ACCOUNT
        userRepository.save(entity).awaitSingle()
        logger.debug("UserDto with userId: $userId deleted")
    }

    override suspend fun changePassword(
        userId: String,
        passwordUpdate: PasswordUpdate,
    ) {
        val entity =
            userRepository.findById(userId).awaitFirstOrNull()
                ?: throw UserNotFoundException(userId)
        if (!passwordEncoder.matches(passwordUpdate.oldPassword, entity.password)) {
            throw WrongPasswordException()
        }
        if (passwordEncoder.matches(passwordUpdate.newPassword, entity.password)) {
            throw SimilarPasswordException()
        }
        val encryptedPassword = passwordEncoder.encode(passwordUpdate.newPassword)
        entity.password = encryptedPassword
        userRepository.save(entity).awaitSingle()
    }

    private suspend fun uploadPortrait(filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()
        return storageService.uploadProfilePortrait(filePart.filename(), contentType, contentBytes)
    }

    fun generatePassword(): String {
        val base64 = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().toByteArray())
        return base64.take(8)
    }
}
