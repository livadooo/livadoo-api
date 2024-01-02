plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(libs.error.handling)

    implementation(projects.storageServiceProxy)

    implementation(projects.utils.springUtils)
    implementation(projects.utils.securityUtils)
}
