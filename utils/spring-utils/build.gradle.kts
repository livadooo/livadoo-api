plugins {
	id("com.livadoo.utils-conventions")
	alias(libs.plugins.spring.boot) apply false
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.plugin.spring)
}

dependencies {
	implementation(libs.error.handling)
	implementation(libs.spring.boot.webflux)
	implementation(libs.kotlinx.coroutines.reactive)
}