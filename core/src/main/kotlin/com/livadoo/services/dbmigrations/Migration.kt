package com.livadoo.services.dbmigrations

import com.livadoo.library.security.domain.ROLE_ADMIN
import com.livadoo.library.security.domain.ROLE_CUSTOMER
import com.livadoo.library.security.domain.ROLE_EDITOR
import com.livadoo.services.user.services.mongodb.entity.AuthorityEntity
import com.livadoo.services.user.services.mongodb.entity.UserEntity
import com.livadoo.services.user.services.mongodb.repository.AuthorityRepository
import com.livadoo.services.user.services.mongodb.repository.UserRepository
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync
import org.springframework.security.crypto.password.PasswordEncoder

@ChangeUnit(id = "create-initial-roles", order = "1")
class InitializeRolesChangeUnit {

    @Execution
    fun execution(authorityRepository: AuthorityRepository) {
        val authorityEntities = listOf(
            AuthorityEntity(ROLE_ADMIN),
            AuthorityEntity(ROLE_EDITOR),
            AuthorityEntity(ROLE_CUSTOMER)
        )

        val subscriberSync = MongoSubscriberSync<AuthorityEntity>()
        authorityRepository.saveAll(authorityEntities).subscribe(subscriberSync)
        subscriberSync.await()
    }

    @RollbackExecution
    fun rollbackExecution() = Unit
}

@ChangeUnit(id = "create-default-admin-user", order = "2")
class CreateDefaultAdminChangeUnit {

    @Execution
    fun execution(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
        val adminUser = UserEntity(
            firstName = "Super",
            lastName = "Admin",
            phoneNumber = "+237670000000",
            authority = ROLE_ADMIN,
            password = passwordEncoder.encode("1234"),
            email = "admin@livadoo.com",
            verified = true
        )
        val customerUser1 = UserEntity(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "+237670000001",
            authority = ROLE_CUSTOMER,
            password = passwordEncoder.encode("1234"),
            email = "customer1@livadoo.com",
            verified = true
        )
        val customerUser2 = UserEntity(
            firstName = "Jane",
            lastName = "Doe",
            phoneNumber = "+237670000002",
            authority = ROLE_CUSTOMER,
            password = passwordEncoder.encode("1234"),
            email = "customer2@livadoo.com",
            verified = true
        )
        val subscriberSync = MongoSubscriberSync<UserEntity>()
        userRepository.saveAll(listOf(adminUser, customerUser1, customerUser2)).subscribe(subscriberSync)
        subscriberSync.await()
    }

    @RollbackExecution
    fun rollbackExecution() = Unit
}