plugins {
    id("com.livadoo.kotlin-conventions")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.plugin.spring)
    jacoco
    alias(libs.plugins.jib)
    id("jacoco-report-aggregation")
}

group = "com.livadoo.services"
version = "0.0.1"

dependencies {
    implementation(projects.jwtSecurityLib)
    implementation(projects.notificationService)
    implementation(projects.userService)
    implementation(projects.customerService)
    implementation(projects.storageService)
    implementation(projects.inventoryService)
    implementation(projects.common)

    implementation(platform(libs.mongock.bom))
    implementation(libs.mongock.mongodb.reactive.driver)
    implementation(libs.mongock.springboot)

    implementation(platform(libs.mongock.bom))
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.mongock.mongodb.reactive.driver)
    implementation(libs.mongock.springboot)
    implementation(libs.spring.boot.data.mongodb.reactive)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.boot.webflux)
    implementation(libs.springfox.boot)
    implementation(libs.error.handling)
}