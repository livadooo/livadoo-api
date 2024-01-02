plugins {
	id("com.livadoo.utils-conventions")
	alias(libs.plugins.spring.boot) apply false
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.kotlin.plugin.spring)
	alias(libs.plugins.kotlin.jvm)
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {
	implementation(libs.jjwt.api)
	implementation(libs.jjwt.impl)
	implementation(libs.jjwt.jackson)
	implementation(libs.kotlinx.coroutines.reactive)
	implementation(libs.spring.boot.security)
	implementation(libs.spring.boot.webflux)

	implementation(projects.utils.exceptionUtils)
}
