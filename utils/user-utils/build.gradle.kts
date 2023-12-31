plugins {
	id("com.livadoo.utils-conventions")
	alias(libs.plugins.spring.boot) apply false
}

dependencies {
	implementation(libs.spring.boot.validation)
	implementation(libs.spring.boot.data.mongodb.reactive)
}