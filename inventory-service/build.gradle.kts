plugins {
    id("com.livadoo.services-conventions")
}

version = "0.0.1"

dependencies {
    implementation(projects.utils.springUtils)

    implementation(project(":storage-service-proxy"))
    implementation(project(":jwt-security-lib"))
    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
}