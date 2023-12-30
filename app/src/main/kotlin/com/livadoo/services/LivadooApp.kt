package com.livadoo.services

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.livadoo.services")
class LivadooApp

fun main(args: Array<String>) {
    runApplication<LivadooApp>(*args)
}