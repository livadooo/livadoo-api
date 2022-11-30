plugins {
	`kotlin-dsl`
}

repositories {
	gradlePluginPortal()
}

dependencies {
	implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.7.21")
	implementation("org.jetbrains.kotlin.plugin.allopen:org.jetbrains.kotlin.plugin.allopen.gradle.plugin:1.7.10")
	implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:3.0.0")
	implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:1.1.0")
}