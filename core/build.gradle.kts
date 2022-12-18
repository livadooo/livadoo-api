plugins {
    id("com.livadoo.kotlin-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    jacoco
}

group = "com.livadoo.services"
version = "0.0.1"

dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:3.3.0")
        mavenBom("io.mongock:mongock-bom:5.1.0")
    }
}

dependencies {
    implementation(project(":jwt-security-lib"))
    implementation(project(":user-service"))
    implementation(project(":notification-service"))
    implementation(project(":customer-service"))
    implementation(project(":storage-service"))
    implementation(project(":inventory-service"))
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.mongock:mongock-springboot")
    implementation("io.mongock:mongodb-reactive-driver")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")
}