plugins {
	id("com.livadoo.services-conventions")
}

dependencies {
	implementation(project(":notification-service-proxy"))
	implementation(projects.utils.springUtils)

	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}
