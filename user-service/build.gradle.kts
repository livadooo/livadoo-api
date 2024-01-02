plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(libs.googlecode.libphonenumber)
    implementation(libs.spring.boot.security)

    implementation(projects.customerServiceProxy)
    implementation(projects.notificationServiceProxy)
    implementation(projects.permissionServiceProxy)
    implementation(projects.roleServiceProxy)
    implementation(projects.shared)
    implementation(projects.storageServiceProxy)
    implementation(projects.userServiceProxy)

    implementation(projects.utils.securityUtils)
    implementation(projects.utils.springUtils)
    implementation(projects.utils.userUtils)
}
