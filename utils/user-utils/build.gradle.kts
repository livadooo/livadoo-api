plugins {
	id("com.livadoo.utils-conventions")
	alias(libs.plugins.spring.boot) apply false
}

dependencies {
	implementation(libs.spring.boot.data.mongodb.reactive)
	implementation(libs.spring.boot.validation)
}
