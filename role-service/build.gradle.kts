plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.roleServiceProxy)
    implementation(projects.shared)

    implementation(projects.utils.securityUtils)
}
