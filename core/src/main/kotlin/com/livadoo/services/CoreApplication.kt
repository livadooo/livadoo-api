package com.livadoo.services

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.livadoo.services")
class CoreApplication

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}