plugins {
    id("com.livadoo.services-conventions")
}

dependencies {
    implementation(libs.googlecode.libphonenumber)

    implementation(projects.phoneValidationServiceProxy)
}
