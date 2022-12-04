plugins {
	id("com.livadoo.services-conventions")
}

version = "0.0.1"

dependencies {
	implementation(project(":notification-service-proxy"))
	implementation(project(":common"))

	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}
