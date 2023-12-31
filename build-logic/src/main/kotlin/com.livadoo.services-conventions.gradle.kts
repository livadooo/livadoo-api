plugins {
	id("com.livadoo.kotlin-conventions")
	id("io.spring.dependency-management")
	kotlin("plugin.spring")
	jacoco
}

group = "com.livadoo.services"

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {
	implementation(project(":utils:exception-utils"))

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.google.code.gson:gson:2.10")

	// Testing libraries
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(module = "mockito-core")
	}
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.0.2")
	testImplementation("io.kotest:kotest-runner-junit5-jvm:5.4.2")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
	testImplementation("com.ninja-squad:springmockk:3.1.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
	systemProperty("spring.profiles.active", "test")
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
//				minimum = "0.80".toBigDecimal()
			}
		}
	}
}

tasks.check {
	dependsOn(tasks.jacocoTestCoverageVerification)
}