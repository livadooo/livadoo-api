plugins {
	`kotlin-dsl`
}

repositories {
	gradlePluginPortal()
}

dependencies {
	implementation(libs.kotlin.plugin.jvm)
	implementation(libs.kotlin.plugin.allopen)
	implementation(libs.spring.boot.plugin)
	implementation(libs.spring.dependency.management.plugin)
	implementation(libs.spotless.plugin)
}