plugins {
    id("com.livadoo.services-conventions")
}

version = "0.0.1"

dependencies {
    implementation(project(":jwt-security-lib"))
    implementation(project(":common"))
    implementation(project(":customer-service-proxy"))
}