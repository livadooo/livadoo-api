plugins {
    id("com.livadoo.services-conventions")
}

version = "0.0.1"

dependencies {
    /*implementation(project(":user-service-proxy"))*/
    implementation(project(":customer-service-proxy"))
    implementation(project(":notification-service-proxy"))
    implementation(project(":jwt-security-lib"))
    implementation(project(":storage-service-proxy"))
    implementation(project(":common"))

    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.1")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.1")
}
