plugins {
    id("com.livadoo.kotlin-conventions")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.plugin.spring)
    jacoco
    alias(libs.plugins.jib)
    id("jacoco-report-aggregation")
}

group = "com.livadoo.services"
version = "0.0.1"

dependencies {
    implementation(projects.accountService)
    implementation(projects.authService)
    implementation(projects.notificationService)
    implementation(projects.userService)
    implementation(projects.customerService)
    implementation(projects.storageService)
    implementation(projects.inventoryService)
    implementation(projects.roleService)
    implementation(projects.otpService)
    implementation(projects.permissionService)
    implementation(projects.utils.securityUtils)

    implementation(platform(libs.mongock.bom))
    implementation(libs.mongock.mongodb.reactive.driver)
    implementation(libs.mongock.springboot)

    implementation(platform(libs.mongock.bom))
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.mongock.mongodb.reactive.driver)
    implementation(libs.mongock.springboot)
    implementation(libs.spring.boot.data.mongodb.reactive)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.boot.webflux)
    implementation(libs.springfox.boot)
    implementation(libs.error.handling)
}

val supportedProfiles = setOf("dev", "prod")

jib {
    val appProfile: String by project
    val isSnapshot = version.toString().endsWith("SNAPSHOT")

    check(supportedProfiles.contains(appProfile)) {
        "Unsupported profile: $appProfile, make sure you set the right profile as appProfile value in gradle.properties"
    }

    from {
        image = "eclipse-temurin:17-jre"
    }

    to {
        val livadooDockerRegistrySnapshotsUrl: String by project
        val livadooDockerRegistryReleasesUrl: String by project
        val livadooDockerRegistryUsername: String by project
        val livadooDockerRegistryPassword: String by project
        val buildNumber: String by project

        val currentVersion = if (isSnapshot) "$version-$buildNumber" else version.toString()

        image = "${if (isSnapshot) livadooDockerRegistrySnapshotsUrl else livadooDockerRegistryReleasesUrl}/livadoo-api-$appProfile"
        tags = setOf(currentVersion, "latest")
        auth {
            username = livadooDockerRegistryUsername
            password = livadooDockerRegistryPassword
        }
    }

    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
        ports = listOf("8080")
        environment = mapOf(
            "SPRING_PROFILES_ACTIVE" to appProfile
        )
        jvmFlags = listOf(
            "-javaagent:/newrelic/newrelic.jar",
            "-Dnewrelic.environment=$appProfile"
        )
    }

    extraDirectories {
        paths {
            path {
                setFrom(file("newrelic/"))
                into = "/newrelic/"
            }
        }
    }
}

tasks.jibDockerBuild {
    if (System.getProperty("os.arch") == "aarch64") {
        jib?.from?.platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
}
