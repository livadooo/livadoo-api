plugins {
	id("com.livadoo.services-conventions")
}

dependencies {
	implementation(libs.spring.boot.mail)
	implementation(libs.spring.boot.thymeleaf)

	implementation(projects.notificationServiceProxy)

	implementation(projects.utils.springUtils)
}
