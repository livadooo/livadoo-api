plugins {
    id("com.livadoo.services-conventions")
}

version = "0.0.1"

dependencies {
    implementation(project(":storage-service-proxy"))
    implementation(project(":jwt-security-lib"))
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-security")
}