plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.permissionServiceProxy)
    implementation(projects.shared)

    implementation(projects.utils.securityUtils)
}
