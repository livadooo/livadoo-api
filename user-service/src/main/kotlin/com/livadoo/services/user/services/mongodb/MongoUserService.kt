package com.livadoo.services.user.services.mongodb

import com.livadoo.common.exceptions.BadInputException
import com.livadoo.common.exceptions.NotAllowedException
import com.livadoo.library.security.domain.ROLE_ADMIN
import com.livadoo.library.security.domain.ROLE_CUSTOMER
import com.livadoo.library.security.domain.ROLE_EDITOR
import com.livadoo.library.security.domain.SYSTEM_ACCOUNT
import com.livadoo.library.security.utils.currentUserId
import com.livadoo.library.security.utils.hasCurrentUserThisAuthority
import com.livadoo.proxy.customer.CustomerServiceProxy
import com.livadoo.proxy.customer.model.CustomerCreate
import com.livadoo.proxy.notification.NotificationServiceProxy
import com.livadoo.proxy.notification.model.CustomerAccount
import com.livadoo.proxy.notification.model.StaffAccount
import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.user.config.PasswordResetKeyProperties
import com.livadoo.services.user.data.PasswordUpdate
import com.livadoo.services.user.data.StaffCreate
import com.livadoo.services.user.data.User
import com.livadoo.services.user.data.UserCreate
import com.livadoo.services.user.data.UserUpdate
import com.livadoo.services.user.exceptions.SecureKeyNotFoundException
import com.livadoo.services.user.exceptions.UserNotFoundException
import com.livadoo.services.user.exceptions.UserWithEmailAlreadyExistsException
import com.livadoo.services.user.exceptions.UserWithPhoneNumberAlreadyExistsException
import com.livadoo.services.user.services.UserService
import com.livadoo.services.user.services.mongodb.entity.AuthorityEntity
import com.livadoo.services.user.services.mongodb.entity.KeyType
import com.livadoo.services.user.services.mongodb.entity.SecureKeyEntity
import com.livadoo.services.user.services.mongodb.entity.UserEntity
import com.livadoo.services.user.services.mongodb.entity.toDto
import com.livadoo.services.user.services.mongodb.repository.AuthorityRepository
import com.livadoo.services.user.services.mongodb.repository.SecureKeyRepository
import com.livadoo.services.user.services.mongodb.repository.UserRepository
import kotlinx.coroutines.flow.first
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
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import java.io.ByteArrayOutputStream
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
    private val customerService: CustomerServiceProxy
) : UserService {

    private val logger = LoggerFactory.getLogger(MongoUserService::class.java)

    override suspend fun createCustomerUser(userCreate: UserCreate) {
        val isCurrentUserAdmin = hasCurrentUserThisAuthority(ROLE_ADMIN).awaitFirstOrDefault(false)
        val isCurrentUserEditor = hasCurrentUserThisAuthority(ROLE_EDITOR).awaitFirstOrDefault(false)
        val isCurrentUserCustomer = hasCurrentUserThisAuthority(ROLE_CUSTOMER).awaitFirstOrDefault(false)
        val hasAdminRole = isCurrentUserEditor || isCurrentUserAdmin

        if (isCurrentUserCustomer) {
            throw NotAllowedException("You are not allowed to create a customer.")
        }

        userRepository.findByEmailIgnoreCase(userCreate.email).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) userRepository.delete(user).awaitSingleOrNull()
                else throw UserWithEmailAlreadyExistsException(userCreate.email)
            }

        userRepository
            .findByPhoneNumber(userCreate.phoneNumber).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) userRepository.delete(user).awaitSingleOrNull()
                else throw UserWithPhoneNumberAlreadyExistsException(userCreate.phoneNumber)
            }

        val authorityEntities = authorityRepository.findAllById(listOf(ROLE_CUSTOMER)).asFlow().toList()
        if (authorityEntities.isEmpty()) {
            throw BadInputException("Invalid authority", "The supplied authority is invalid.")
        }

        val entity = UserEntity(
            userCreate.firstName,
            userCreate.lastName,
            userCreate.phoneNumber,
            authorityEntities,
            passwordEncoder.encode(userCreate.password),
            userCreate.email,
            null,
            verified = hasAdminRole,
            createdBy = currentUserId.awaitFirstOrDefault(SYSTEM_ACCOUNT),
            createdAt = Instant.now()
        )

        val user = userRepository.save(entity).awaitSingle().toDto()
        if (hasAdminRole) {
            mono {
                val customerCreate = CustomerCreate(user.userId!!)
                customerService.createCustomer(customerCreate).also { logger.info("Successfully created customer account") }
            }.subscribe(null) { error ->
                logger.error("An error occurred while creating customer account ---> $error")
            }
        } else {
            val userId = user.userId!!
            val email = user.email

            val key = Random.nextInt(IntRange(100000, 999999)).toString()
            val keyValidityTime = passwordResetKeyProperties.validityTimeInHours
            val expiresAt = Instant.now().plus(keyValidityTime.toLong(), ChronoUnit.HOURS)

            val keyEntity = SecureKeyEntity(key, userId, KeyType.ACCOUNT_VERIFY, expiresAt, Instant.now(), Instant.now())

            saveSecureKey(keyEntity, userId, key, expiresAt)

            mono {
                val credentials = CustomerAccount(userId, email, key)
                notificationService.sendCustomerAccountVerificationEmail(credentials).also { logger.info(it) }
            }.subscribe(null) { error ->
                logger.error("An error occurred while sending customer account verification email ---> $error")
            }
        }

        logger.info("Customer user created")
    }

    override suspend fun createStaffUser(staffCreate: StaffCreate) {
        val isCurrentUserAdmin = hasCurrentUserThisAuthority(ROLE_ADMIN).awaitFirstOrDefault(false)

        if (!isCurrentUserAdmin) {
            throw NotAllowedException("You are not allowed to create an administrator.")
        }

        userRepository
            .findByEmailIgnoreCase(staffCreate.email).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) userRepository.delete(user).awaitSingleOrNull()
                else throw UserWithEmailAlreadyExistsException(staffCreate.email)
            }

        userRepository
            .findByPhoneNumber(staffCreate.phoneNumber).awaitSingleOrNull()
            ?.also { user ->
                if (!user.verified) userRepository.delete(user).awaitSingleOrNull()
                else throw UserWithPhoneNumberAlreadyExistsException(staffCreate.phoneNumber)
            }

        val authorities = listOf(staffCreate.authority)
        val authorityEntities = authorityRepository.findAllById(authorities).asFlow().toList()
        if (authorityEntities.isEmpty()) {
            throw BadInputException("Invalid authority", "The supplied authority is invalid.")
        }
        val generatedPassword = generatePassword()
        val entity = UserEntity(
            staffCreate.firstName,
            staffCreate.lastName,
            staffCreate.phoneNumber,
            authorityEntities,
            passwordEncoder.encode(generatedPassword),
            staffCreate.email,
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

    override suspend fun verifyAccount(key: String): User {
        val activationKey = secureKeyRepository.findByKey(key).awaitSingleOrNull()
            ?.takeIf { it.expirationDate >= Instant.now() }
            ?: throw SecureKeyNotFoundException(detail = "Clé d'activation introuvable ou expirée")

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

        val entity = userRepository.findById(userUpdate.userId).awaitSingleOrNull()
            ?: throw UserNotFoundException(userUpdate.userId)

        userRepository.findByPhoneNumber(userUpdate.phoneNumber).awaitSingleOrNull()
            ?.also { existingUserWithPhone ->
                if (existingUserWithPhone.id != userUpdate.userId)
                    throw UserWithPhoneNumberAlreadyExistsException(userUpdate.phoneNumber)
            }

        val isCurrentUserAdmin = hasCurrentUserThisAuthority(ROLE_ADMIN).awaitFirstOrDefault(false)
        val currentAuthorityEntity = entity.authorities.first()
        var authorities: List<AuthorityEntity> = listOf(currentAuthorityEntity)

        if (isCurrentUserAdmin) {
            val authorityEntity = authorityRepository.findAllById(listOf(userUpdate.authority)).asFlow().first()
            if (currentAuthorityEntity.name != authorityEntity.name) {
                authorities = listOf(authorityEntity)
            }
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
            this.authorities = authorities
        }

        return userRepository.save(entity).awaitSingle()
            .toDto()
            .also { logger.debug("User with userId: ${it.userId} was successfully updated") }
    }

    override suspend fun updateUserAvatar(filePart: FilePart, userId: String) {
        val userEntity = userRepository.findById(userId).awaitSingleOrNull()
            ?: throw UserNotFoundException(userId)

        val contentType = filePart.headers()["content-type"]!![0]
        val bytesOutputStream = ByteArrayOutputStream()

        filePart.content()
            .map { it.asByteBuffer().array() }
            .collectList()
            .awaitFirst()
            .forEach { bytes -> bytesOutputStream.write(bytes) }

        val avatarUuid = storageService.uploadFile(filePart.filename(), contentType, bytesOutputStream.toByteArray())
        userEntity.avatarId = avatarUuid

        userRepository.save(userEntity).awaitSingle()
    }

    override suspend fun getAdminUsers(pageable: Pageable): Pair<List<User>, Long> {
        return with(authorityRepository.findAllById(listOf(ROLE_ADMIN, ROLE_EDITOR)).asFlow().toList()) {
            val users = userRepository
                .findByAuthoritiesContainingAndDeletedFalse(this, pageable)
                .asFlow()
                .map { it.toDto() }
                .toList()
            val usersCount = userRepository.countAllByAuthoritiesContainingAndDeletedFalse(this).awaitSingle()
            users to usersCount
        }
    }

    override suspend fun getCustomerUsers(pageable: Pageable): Pair<List<User>, Long> {
        return with(authorityRepository.findAllById(listOf(ROLE_ADMIN, ROLE_EDITOR)).asFlow().toList()) {
            val users = userRepository
                .findByAuthoritiesContainingAndDeletedFalse(this, pageable)
                .asFlow()
                .map { it.toDto() }
                .toList()
            val usersCount = userRepository.countAllByAuthoritiesContainingAndDeletedFalse(this).awaitSingle()
            users to usersCount
        }
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
            throw BadInputException("Incorrect password", "Current password is incorrect.")
        }
        if (passwordEncoder.matches(passwordUpdate.newPassword, entity.password)) {
            throw BadInputException("Too similar password", "The new password is very close to the new password")
        }
        val encryptedPassword = passwordEncoder.encode(passwordUpdate.newPassword)
        entity.password = encryptedPassword
        userRepository.save(entity).awaitSingle()
    }

    private suspend fun saveSecureKey(entity: SecureKeyEntity, userId: String, key: String, expiresAt: Instant) {
        try {
            secureKeyRepository.save(entity).awaitSingle()
        } catch (exception: DuplicateKeyException) {
            val existingActivationKey = secureKeyRepository.findByUserId(userId).awaitSingle()
            existingActivationKey.apply {
                updatedAt = Instant.now()
                this.key = key
                expirationDate = expiresAt
            }
            secureKeyRepository.save(existingActivationKey).awaitSingle()
        }
    }

    fun generatePassword(): String {
        val base64 = Base64Utils.encodeToString(UUID.randomUUID().toString().toByteArray())
        return base64.take(8)
    }
}