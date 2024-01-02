plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(libs.jjwt.api)
    implementation(libs.jjwt.jackson)
    implementation(libs.spring.boot.security)

    implementation(projects.notificationServiceProxy)
    implementation(projects.authServiceProxy)
    implementation(projects.userServiceProxy)

    implementation(projects.utils.securityUtils)
    implementation(projects.utils.springUtils)
    implementation(projects.utils.userUtils)
}
