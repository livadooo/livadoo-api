plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(libs.azure.storage.blob)
    implementation(libs.error.handling)
    implementation(platform("com.google.cloud:spring-cloud-gcp-dependencies:3.0.0"))
    implementation("com.google.cloud:spring-cloud-gcp-starter-storage")

    implementation(projects.storageServiceProxy)

    implementation(projects.utils.springUtils)
}
