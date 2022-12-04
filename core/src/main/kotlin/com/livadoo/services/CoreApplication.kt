package com.livadoo.services

import com.livadoo.library.security.config.SecurityProperties
import com.livadoo.services.notification.config.ZeptoMailProperties
import com.livadoo.services.user.config.PasswordResetKeyProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(value = ["com.livadoo.services", "com.livadoo.library.security.config"])
@EnableConfigurationProperties(value = [ZeptoMailProperties::class, PasswordResetKeyProperties::class, SecurityProperties::class])
class CoreApplication

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}