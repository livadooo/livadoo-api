package com.livadoo.services.dbmigrations

import com.livadoo.services.user.services.mongodb.entity.AuthorityEntity
import com.livadoo.services.user.services.mongodb.entity.UserEntity
import com.livadoo.services.user.services.mongodb.repository.AuthorityRepository
import com.livadoo.services.user.services.mongodb.repository.UserRepository
import com.livadoo.utils.security.domain.ROLE_ADMIN
import com.livadoo.utils.security.domain.ROLE_CUSTOMER
import com.livadoo.utils.security.domain.ROLE_EDITOR
import com.livadoo.utils.security.domain.ROLE_SUPER_ADMIN
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync
import org.springframework.security.crypto.password.PasswordEncoder

@ChangeUnit(id = "create-initial-roles", order = "1")
class InitializeRolesChangeUnit {
    @Execution
    fun execution(authorityRepository: AuthorityRepository) {
        val authorityEntities =
            listOf(
                AuthorityEntity(ROLE_SUPER_ADMIN),
                AuthorityEntity(ROLE_ADMIN),
                AuthorityEntity(ROLE_EDITOR),
                AuthorityEntity(ROLE_CUSTOMER),
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
    fun execution(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder,
    ) {
        val superAdminUser =
            UserEntity(
                firstName = "Super",
                lastName = "Admin",
                phoneNumber = "+237670000000",
                authority = ROLE_SUPER_ADMIN,
                password = passwordEncoder.encode("1234"),
                email = "admin@livadoo.com",
                verified = true,
            )
        val subscriberSync = MongoSubscriberSync<UserEntity>()
        userRepository.save(superAdminUser).subscribe(subscriberSync)
        subscriberSync.await()
    }

    @RollbackExecution
    fun rollbackExecution() = Unit
}
