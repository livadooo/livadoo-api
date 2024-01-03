plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(libs.spring.boot.security)

    implementation(projects.authoritySearchServiceProxy)
    implementation(projects.customerServiceProxy)
    implementation(projects.notificationServiceProxy)
    implementation(projects.phoneValidationServiceProxy)
    implementation(projects.permissionServiceProxy)
    implementation(projects.roleServiceProxy)
    implementation(projects.shared)
    implementation(projects.storageServiceProxy)
    implementation(projects.userServiceProxy)

    implementation(projects.utils.securityUtils)
    implementation(projects.utils.springUtils)
    implementation(projects.utils.userUtils)
}
