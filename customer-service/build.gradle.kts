plugins {
    id("com.livadoo.services-conventions")
}

version = "0.0.1"

dependencies {
    implementation(project(":jwt-security-lib"))
    implementation(project(":customer-service-proxy"))
    implementation(project(":user-service-proxy"))
    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")
}