import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
	kotlin("jvm")
	id("com.diffplug.spotless")
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	withSourcesJar()
}

repositories {
	mavenCentral()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

configure<SpotlessExtension> {
	ratchetFrom = "origin/main"
	kotlin {
		ktlint("0.48.2")
	}
}
