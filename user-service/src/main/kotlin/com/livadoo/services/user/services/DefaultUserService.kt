package com.livadoo.services.user.services

import com.livadoo.proxy.authority.search.AuthoritySearchServiceProxy
import com.livadoo.proxy.customer.CustomerServiceProxy
import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.ProxyLanguage
import com.livadoo.proxy.permission.PermissionServiceProxy
import com.livadoo.proxy.phone.validation.PhoneValidationServiceProxy
import com.livadoo.proxy.role.RoleServiceProxy
import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.user.data.CustomerUserCreate
import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffUserCreate
import com.livadoo.services.user.data.UserUpdate
import com.livadoo.services.user.exceptions.DuplicateEmailException
import com.livadoo.services.user.exceptions.DuplicatePhoneNumberException
import com.livadoo.services.user.exceptions.InvalidStaffRolesException
import com.livadoo.services.user.exceptions.SimilarPasswordException
import com.livadoo.services.user.exceptions.UserEmailNotFoundException
import com.livadoo.services.user.exceptions.UserNotFoundException
import com.livadoo.services.user.exceptions.WrongPasswordException
import com.livadoo.shared.extension.containsExceptionKey
import com.livadoo.utils.exception.InternalErrorException
import com.livadoo.utils.security.config.AppSecurityContext
import com.livadoo.utils.spring.extractContent
import com.livadoo.utils.user.Language
import com.livadoo.utils.user.UserDto
import com.livadoo.utils.user.UserEntity
import com.livadoo.utils.user.buildUserId
import com.livadoo.utils.user.toDto
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.Base64
import java.util.UUID

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
    private val notificationService: NotificationServiceProxy,
    private val passwordEncoder: PasswordEncoder,
    private val storageService: StorageServiceProxy,
    private val customerServiceProxy: CustomerServiceProxy,
    private val securityContext: AppSecurityContext,
    private val permissionServiceProxy: PermissionServiceProxy,
    private val roleServiceProxy: RoleServiceProxy,
    private val phoneValidationServiceProxy: PhoneValidationServiceProxy,
    private val clock: Clock,
    private val authoritySearchServiceProxy: AuthoritySearchServiceProxy,
) : UserService {
    override suspend fun createCustomerUser(customerUserCreate: CustomerUserCreate): UserDto {
        val phoneNumber = phoneValidationServiceProxy.validate(
            phoneNumber = customerUserCreate.phoneNumber,
            regionCode = customerUserCreate.regionCode,
        )

        var userEntity = UserEntity(
            firstName = customerUserCreate.firstName,
            lastName = customerUserCreate.lastName,
            language = Language.FR,
            userId = buildUserId,
            activated = true,
            phoneNumber = phoneNumber,
            email = null,
            roleIds = emptyList(),
            permissionIds = emptyList(),
            password = null,
            createdAt = clock.instant(),
            createdBy = securityContext.getCurrentUserId(),
        )

        userEntity = saveUser(userEntity)

        customerServiceProxy.createCustomer(
            userId = userEntity.userId,
            createdBy = securityContext.getCurrentUserIdOrDefault(),
        )
        return userEntity.toDto(permissions = emptyList(), roles = emptyList())
    }

    override suspend fun createStaffUser(staffUserCreate: StaffUserCreate): UserDto {
        val phoneNumber = phoneValidationServiceProxy.validate(
            phoneNumber = staffUserCreate.phoneNumber,
            regionCode = staffUserCreate.regionCode,
        )
        val roleIds = roleServiceProxy.getRolesByIds(staffUserCreate.roleIds)
        if (roleIds.size != staffUserCreate.roleIds.size) {
            throw InvalidStaffRolesException()
        }
        val permissionIds = permissionServiceProxy.getBasePermissionsByRoles(roleIds)
        val password = randomPassword
        var userEntity = UserEntity(
            firstName = staffUserCreate.firstName,
            lastName = staffUserCreate.lastName,
            language = Language.FR,
            userId = buildUserId,
            phoneNumber = phoneNumber,
            email = staffUserCreate.email,
            activated = true,
            roleIds = roleIds,
            permissionIds = permissionIds,
            password = passwordEncoder.encode(password),
            createdAt = clock.instant(),
            createdBy = securityContext.getCurrentUserId(),
        )
        notificationService.notifyStaffAccountCreated(
            firstName = staffUserCreate.firstName,
            email = staffUserCreate.email,
            password = password,
            language = ProxyLanguage.valueOf(userEntity.language.name),
        )
        userEntity = saveUser(userEntity)
        val authorities = authoritySearchServiceProxy.getUserAuthorities(userEntity.userId)
        return userEntity.toDto(roles = authorities.roles, permissions = authorities.permissions)
    }

    override suspend fun updateUser(userUpdate: UserUpdate): UserDto {
        var userEntity = getUser(userUpdate.userId)

        userEntity.apply {
            firstName = userUpdate.firstName
            lastName = userUpdate.lastName
            address = userUpdate.address
            city = userUpdate.city
            country = userUpdate.country
            updatedAt = clock.instant()
            updatedBy = securityContext.getCurrentUserId()
        }
        userEntity = saveUser(userEntity)
        val authorities = authoritySearchServiceProxy.getUserAuthorities(userEntity.userId)
        return userEntity.toDto(roles = authorities.roles, permissions = authorities.permissions)
    }

    override suspend fun updateUserPortrait(filePart: FilePart, userId: String): String {
        val userEntity = getUser(userId)
        userEntity.photoUrl = uploadPortrait(filePart)
        return saveUser(userEntity).photoUrl!!
    }

    override suspend fun deleteUserPortrait(userId: String) {
        val userEntity = userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)

        userEntity.photoUrl = null
        saveUser(userEntity)
    }

    override suspend fun getUserById(userId: String): UserDto {
        val userEntity = getUser(userId)
        val authorities = authoritySearchServiceProxy.getUserAuthorities(userId)
        return userEntity.toDto(roles = authorities.roles, permissions = authorities.permissions)
    }

    override suspend fun getUserByEmail(email: String): UserDto {
        val userEntity = userRepository.findByEmailIgnoreCase(email)
            ?: throw UserEmailNotFoundException(email)

        val authorities = authoritySearchServiceProxy.getUserAuthorities(userEntity.userId)
        return userEntity.toDto(roles = authorities.roles, permissions = authorities.permissions)
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserDto {
        val userEntity = userRepository.findByPhoneNumber(phoneNumber)
            ?: throw UserNotFoundException(phoneNumber)

        val authorities = authoritySearchServiceProxy.getUserAuthorities(userEntity.userId)
        return userEntity.toDto(roles = authorities.roles, permissions = authorities.permissions)
    }

    override suspend fun blockUser(userId: String) {
        val userEntity = userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)

        userEntity.apply {
            blocked = true
            updatedAt = clock.instant()
            updatedBy = securityContext.getCurrentUserId()
        }
        saveUser(userEntity)
    }

    override suspend fun deleteUser(userId: String) {
        val userEntity = userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)

        userEntity.apply {
            deleted = true
            updatedAt = clock.instant()
            updatedBy = securityContext.getCurrentUserId()
        }
        saveUser(userEntity)
    }

    override suspend fun updateStaffPassword(
        userId: String,
        passwordUpdate: PasswordUpdate,
    ) {
        val userEntity = getUser(userId)
        if (!passwordEncoder.matches(passwordUpdate.oldPassword, userEntity.password)) {
            throw WrongPasswordException()
        }
        if (passwordEncoder.matches(passwordUpdate.newPassword, userEntity.password)) {
            throw SimilarPasswordException()
        }
        val encryptedPassword = passwordEncoder.encode(passwordUpdate.newPassword)
        userEntity.password = encryptedPassword
        saveUser(userEntity)
    }

    private suspend fun uploadPortrait(filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()
        return storageService.uploadProfilePortrait(filePart.filename(), contentType, contentBytes)
    }

    val randomPassword: String
        get() {
            val base64 = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().toByteArray())
            return base64.take(8)
        }

    private suspend fun saveUser(userEntity: UserEntity): UserEntity {
        return try {
            userRepository.save(userEntity)
        } catch (exception: DuplicateKeyException) {
            if (exception.message!!.containsExceptionKey("userId")) {
                saveUser(userEntity.copy(userId = buildUserId))
            } else if (exception.message!!.containsExceptionKey("email")) {
                throw DuplicateEmailException(userEntity.email!!)
            } else if (exception.message!!.containsExceptionKey("phoneNumber")) {
                throw DuplicatePhoneNumberException(userEntity.email!!)
            } else {
                throw InternalErrorException()
            }
        }
    }

    private suspend fun getUser(userId: String): UserEntity {
        return userRepository.findByUserId(userId)
            ?: throw UserNotFoundException(userId)
    }
}
