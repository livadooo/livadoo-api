plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.utils.securityUtils)
    implementation(projects.utils.springUtils)
    implementation(projects.utils.userUtils)
    implementation(project(":customer-service-proxy"))
    implementation(project(":notification-service-proxy"))
    implementation(project(":storage-service-proxy"))
    implementation(project(":user-service-proxy"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.1")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.1")
}
