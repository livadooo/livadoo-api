plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(projects.shared)

    implementation(projects.utils.securityUtils)
    implementation(projects.utils.userUtils)
}
