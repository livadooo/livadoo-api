plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.utils.springUtils)
    implementation(projects.utils.userUtils)
    implementation(projects.utils.securityUtils)
    implementation(projects.notificationServiceProxy)
    implementation(projects.authServiceProxy)
    implementation(projects.userServiceProxy)
    implementation(libs.spring.boot.security)
    implementation(libs.jjwt.api)
    implementation(libs.jjwt.jackson)
}
