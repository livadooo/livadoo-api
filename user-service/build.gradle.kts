plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.utils.springUtils)
    implementation(projects.utils.securityUtils)
    implementation(project(":customer-service-proxy"))
    implementation(project(":notification-service-proxy"))
    implementation(project(":storage-service-proxy"))
    implementation(project(":user-service-proxy"))
    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.1")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.1")
}
