plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(libs.error.handling)

    implementation(projects.customerServiceProxy)
    implementation(projects.userServiceProxy)

    implementation(projects.utils.securityUtils)
}
