plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.utils.springUtils)
    implementation(project(":storage-service-proxy"))

    implementation(platform("com.google.cloud:spring-cloud-gcp-dependencies:3.0.0"))
    implementation("com.google.cloud:spring-cloud-gcp-starter-storage")
    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.0.0")
}
