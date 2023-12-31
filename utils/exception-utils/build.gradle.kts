plugins {
	id("com.livadoo.utils-conventions")
	alias(libs.plugins.spring.boot) apply false
}

dependencies {
	implementation(libs.error.handling)
	implementation(libs.spring.boot.webflux)
}