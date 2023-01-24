package com.livadoo.services.user.services.mongodb

import com.livadoo.library.security.domain.ADMIN_ROLES
import com.livadoo.library.security.domain.ROLE_ADMIN
import com.livadoo.library.security.domain.ROLE_CUSTOMER
import com.livadoo.library.security.domain.ROLE_EDITOR
import com.livadoo.library.security.domain.ROLE_SUPER_ADMIN
import com.livadoo.library.security.domain.STAFF_ROLES
import com.livadoo.library.security.domain.SYSTEM_ACCOUNT
import com.livadoo.library.security.utils.currentAuthUser
import com.livadoo.library.security.utils.currentUserId
import com.livadoo.library.security.utils.hasCurrentUserThisAuthority
import com.livadoo.proxy.customer.CustomerServiceProxy
import com.livadoo.proxy.customer.model.CustomerCreate
import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.model.CustomerAccount
import com.livadoo.proxy.notification.model.StaffAccount
import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.common.utils.extractContent
import com.livadoo.services.user.config.PasswordResetKeyProperties
import com.livadoo.services.user.data.CustomerUserCreate
import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffUserCreate
import com.livadoo.services.user.data.User
import com.livadoo.services.user.data.UserUpdate
import com.livadoo.services.user.exceptions.AuthorityNotFoundException
import com.livadoo.services.user.exceptions.NotAllowedToCreateUserException
import com.livadoo.services.user.exceptions.NotAllowedToEditUserException
import com.livadoo.services.user.exceptions.SecureKeyExpiredException
import com.livadoo.services.user.exceptions.SimilarPasswordException
import com.livadoo.services.user.exceptions.UserEmailTakenException
import com.livadoo.services.user.exceptions.UserNotFoundException
import com.livadoo.services.user.exceptions.UserPhoneTakenException
import com.livadoo.services.user.exceptions.WrongPasswordException
import com.livadoo.services.user.extensions.saveOrUpdate
import com.livadoo.services.user.services.UserService
import com.livadoo.services.user.services.mongodb.entity.KeyType
import com.livadoo.services.user.services.mongodb.entity.SecureKeyEntity
import com.livadoo.services.user.services.mongodb.entity.UserEntity
import com.livadoo.services.user.services.mongodb.entity.toDto
import com.livadoo.services.user.services.mongodb.repository.AuthorityRepository
import com.livadoo.services.user.services.mongodb.repository.SecureKeyRepository
import com.livadoo.services.user.services.mongodb.repository.UserRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class MongoUserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val authorityRepository: AuthorityRepository,
    private val notificationService: NotificationServiceProxy,
    private val passwordEncoder: PasswordEncoder,
    private val passwordResetKeyProperties: PasswordResetKeyProperties,
    private val secureKeyRepository: SecureKeyRepository,
    private val storageService: StorageServiceProxy,
    private val customerService: CustomerServiceProxy,
) : UserService {

    private val logger = LoggerFactory.getLogger(MongoUserService::class.java)

    override suspend fun createCustomerUser(customerUserCreate: CustomerUserCreate) {
        val isCurrentUserCustomer = hasCurrentUserThisAuthority(ROLE_CUSTOMER).awaitFirstOrDefault(false)
        val isCurrentUserStaff = currentAuthUser.awaitFirstOrNull()?.isStaff ?: false

        if (isCurrentUserCustomer) throw NotAllowedToCreateUserException()

        checkEmailAndPassword(customerUserCreate.email, customerUserCreate.phoneNumber)

        val entity = UserEntity(
            customerUserCreate.firstName,
            customerUserCreate.lastName,
            customerUserCreate.phoneNumber,
            ROLE_CUSTOMER,
            passwordEncoder.encode(customerUserCreate.password),
            customerUserCreate.email,
            null,
            verified = isCurrentUserStaff,
            createdBy = currentUserId.awaitFirstOrDefault(SYSTEM_ACCOUNT),
            createdAt = Instant.now()
        )

        val user = userRepository.save(entity).awaitSingle().toDto()
        if (isCurrentUserStaff) {
            mono {
                val customerCreate = CustomerCreate(user.userId!!)
                customerService.createCustomer(customerCreate).also { logger.info("Successfully created customer account") }
            }.subscribe(null) { error ->
                logger.error("An error occurred while creating customer account ---> $error")
            }
        } else {
            val userId = user.userId!!
            val key = Random.nextInt(IntRange(100000, 999999)).toString()
            val keyValidityTime = passwordResetKeyProperties.validityTimeInHours
            val expiresAt = Instant.now().plus(keyValidityTime.toLong(), ChronoUnit.HOURS)

            val verificationKeyEntity = SecureKeyEntity(key, userId, KeyType.ACCOUNT_VERIFY, expiresAt, Instant.now(), Instant.now())
            verificationKeyEntity.saveOrUpdate(secureKeyRepository, userId, key, expiresAt)

            mono {
                val credentials = CustomerAccount(userId, user.email, key)
                notificationService.sendCustomerAccountVerificationEmail(credentials).also { logger.info(it) }
            }.subscribe(null) { error ->
                logger.error("An error occurred while sending customer account verification email ---> $error")
            }
        }

        logger.info("Customer user created")
    }

    override suspend fun createStaffUser(staffUserCreate: StaffUserCreate) {
        val isCurrentUserSuperAdmin = hasCurrentUserThisAuthority(ROLE_SUPER_ADMIN).awaitFirstOrDefault(false)
        val isCurrentUserAdmin = hasCurrentUserThisAuthority(ROLE_ADMIN).awaitFirstOrDefault(false)

        val isGranted = (isCurrentUserAdmin && staffUserCreate.authority == ROLE_EDITOR) ||
                (isCurrentUserSuperAdmin && staffUserCreate.authority == ROLE_ADMIN)

        if (!isGranted) throw NotAllowedToCreateUserException()

        checkEmailAndPassword(staffUserCreate.email, staffUserCreate.phoneNumber)

        val authority = staffUserCreate.authority
        authorityRepository.findById(authority).awaitFirstOrNull()
            ?: throw AuthorityNotFoundException(staffUserCreate.authority)

        val generatedPassword = generatePassword()
        val entity = UserEntity(
            staffUserCreate.firstName,
            staffUserCreate.lastName,
            staffUserCreate.phoneNumber,
            authority,
            passwordEncoder.encode(generatedPassword),
            staffUserCreate.email,
            null,
            verified = true,
            createdBy = currentUserId.awaitFirstOrDefault(SYSTEM_ACCOUNT),
            createdAt = Instant.now()
        )

        val user = userRepository.save(entity).awaitSingle().toDto()
        mono {
            val staffAccount = StaffAccount(user.userId!!, user.email, generatedPassword, user.firstName)
            notificationService.sendStaffAccountCreateConfirmationEmail(staffAccount).also { logger.info(it) }
        }.subscribe(null) { error ->
            logger.error("An error occurred while sending staff credentials email ---> $error")
        }

        logger.info("Staff user created")
    }

    private suspend fun checkEmailAndPassword(email: String, phoneNumber: String) {
        userRepository
            .findByEmailIgnoreCase(email).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) userRepository.delete(user).awaitSingleOrNull()
                else throw UserEmailTakenException(email)
            }

        userRepository
            .findByPhoneNumber(phoneNumber).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) userRepository.delete(user).awaitSingleOrNull()
                else throw UserPhoneTakenException(phoneNumber)
            }
    }

    override suspend fun verifyAccount(key: String): User {
        val activationKey = secureKeyRepository.findByKey(key).awaitSingleOrNull()
            ?.takeIf { it.expirationDate >= Instant.now() }
            ?: throw SecureKeyExpiredException(key)

        val user = userRepository.findById(activationKey.userId).awaitSingleOrNull()
            ?.apply {
                verified = true
                updatedAt = Instant.now()
                updatedBy = currentUserId.awaitFirstOrDefault(SYSTEM_ACCOUNT)
            }
            ?.also {
                userRepository.save(it).awaitSingle()
                secureKeyRepository.delete(activationKey).awaitSingleOrNull()
            }?.toDto()
            ?: throw UserNotFoundException(activationKey.userId)

        mono {
            val customerCreate = CustomerCreate(user.userId!!)
            customerService.createCustomer(customerCreate).also { logger.info("Successfully created customer account") }
        }.subscribe(null) { error ->
            logger.error("An error occurred while creating customer account ---> $error")
        }

        return user
    }

    override suspend fun updateUser(userUpdate: UserUpdate): User {
        logger.debug("Updating user with userId: ${userUpdate.userId}")

        val isCurrentUserSuperAdmin = hasCurrentUserThisAuthority(ROLE_SUPER_ADMIN).awaitFirstOrDefault(false)
        val isCurrentUserAdmin = hasCurrentUserThisAuthority(ROLE_ADMIN).awaitFirstOrDefault(false)
        val isCurrentUserEditor = hasCurrentUserThisAuthority(ROLE_EDITOR).awaitFirstOrDefault(false)
        val isCurrentUserCustomer = hasCurrentUserThisAuthority(ROLE_CUSTOMER).awaitFirstOrDefault(false)

        val entity = userRepository.findById(userUpdate.userId).awaitSingleOrNull()
            ?: throw UserNotFoundException(userUpdate.userId)

        val isGranted = isCurrentUserSuperAdmin && entity.authority in STAFF_ROLES ||
                isCurrentUserAdmin && entity.authority in ADMIN_ROLES ||
                isCurrentUserEditor && entity.authority in listOf(ROLE_EDITOR, ROLE_CUSTOMER) ||
                isCurrentUserCustomer && entity.authority == ROLE_CUSTOMER

        if (!isGranted) throw NotAllowedToEditUserException()

        userRepository.findByPhoneNumber(userUpdate.phoneNumber).awaitSingleOrNull()
            ?.also { existingUserWithPhone ->
                if (existingUserWithPhone.id != userUpdate.userId) throw UserPhoneTakenException(userUpdate.phoneNumber)
            }
        val canUpdateAuthority = (isCurrentUserAdmin && userUpdate.authority == ROLE_EDITOR) ||
                (isCurrentUserSuperAdmin && userUpdate.authority == ROLE_ADMIN)

        entity.apply {
            firstName = userUpdate.firstName
            if (canUpdateAuthority) authority = userUpdate.authority!!
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
            .also { logger.debug("User with userId: ${it.userId} was successfully updated") }
    }

    override suspend fun updateUserPortrait(filePart: FilePart, userId: String): String {
        val userEntity = userRepository.findById(userId).awaitSingleOrNull()
            ?: throw UserNotFoundException(userId)

        userEntity.portraitUrl = uploadPortrait(filePart)
        return userRepository.save(userEntity).awaitSingle().portraitUrl!!
    }

    override suspend fun deleteUserPortrait(userId: String) {
        val userEntity = userRepository.findById(userId).awaitSingleOrNull()
            ?: throw UserNotFoundException(userId)

        userEntity.portraitUrl = null
        userRepository.save(userEntity).awaitSingle()
    }

    override suspend fun getStaffUsers(pageRequest: PageRequest, query: String): Page<User> {
        return searchUsers(pageRequest, query, listOf(ROLE_ADMIN, ROLE_EDITOR))
    }

    override suspend fun getCustomerUsers(pageRequest: PageRequest, query: String): Page<User> {
        return searchUsers(pageRequest, query, listOf(ROLE_CUSTOMER))
    }

    override suspend fun getUsersByIds(userIds: List<String>): List<User> {
        return userRepository.findAllByIdIn(userIds)
            .map { it.toDto() }
            .asFlow()
            .toList()
    }

    override suspend fun getUser(userId: String): User {
        logger.debug("Getting user with userId: $userId")
        return userRepository.findById(userId).map { it.toDto() }.awaitFirstOrNull()
            ?.also { logger.debug("user found with Id: ${it.userId}") }
            ?: throw UserNotFoundException(userId = userId)
    }

    override suspend fun blockUser(userId: String) {
        logger.debug("Blocking user with userId: $userId")
        val entity = userRepository.findById(userId).awaitFirstOrNull()
            ?: throw UserNotFoundException(userId)


        entity.blocked = true
        entity.updatedAt = Instant.now()
        entity.updatedBy = SYSTEM_ACCOUNT
        userRepository.save(entity).awaitSingle()
        logger.debug("User with userId: $userId blocked")
    }

    override suspend fun deleteUser(userId: String) {
        logger.debug("Deleting user with userId: $userId")
        val entity = userRepository.findById(userId).awaitFirstOrNull()
            ?: throw UserNotFoundException(userId)

        entity.deleted = true
        entity.updatedAt = Instant.now()
        entity.updatedBy = SYSTEM_ACCOUNT
        userRepository.save(entity).awaitSingle()
        logger.debug("User with userId: $userId deleted")
    }

    override suspend fun getAuthorities(): List<String> {
        return authorityRepository.findAll()
            .asFlow()
            .map { it.name }
            .toList()
    }

    override suspend fun changePassword(userId: String, passwordUpdate: PasswordUpdate) {
        val entity = userRepository.findById(userId).awaitFirstOrNull()
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

    private suspend fun searchUsers(pageRequest: PageRequest, query: String, authorities: List<String>): Page<User> {
        return userRepository
            .findUsersByAuthoritiesAndFirstNameOrLastnameOrEmailOrPhoneNumber(authorities, query, pageRequest)
            .map { it.toDto() }
            .collectList()
            .zipWith(userRepository.countUsersByAuthoritiesAndFirstNameOrLastnameOrEmailOrPhoneNumber(authorities, query))
            .map { PageImpl(it.t1, pageRequest, it.t2) }
            .awaitFirst()
    }

    private suspend fun uploadPortrait(filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()
        return storageService.uploadProfilePortrait(filePart.filename(), contentType, contentBytes)
    }

    fun generatePassword(): String {
        val base64 = Base64Utils.encodeToString(UUID.randomUUID().toString().toByteArray())
        return base64.take(8)
    }
}