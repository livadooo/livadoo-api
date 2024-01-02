plugins {
	id("com.livadoo.services-conventions")
}

dependencies {
	implementation(libs.sendgrid)
	implementation(libs.twilio)
	implementation(libs.spring.boot.mail)
	implementation(libs.spring.boot.thymeleaf)

	implementation(projects.notificationServiceProxy)

	implementation(projects.utils.springUtils)
}
