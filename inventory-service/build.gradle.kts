plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.utils.springUtils)
    implementation(projects.utils.securityUtils)

    implementation(project(":storage-service-proxy"))
    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
