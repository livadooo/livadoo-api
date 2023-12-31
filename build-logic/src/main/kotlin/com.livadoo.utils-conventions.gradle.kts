plugins {
    id("com.livadoo.kotlin-conventions")
}

group = "com.livadoo.utils"

dependencies {
    testImplementation("io.kotest:kotest-assertions-core:5.4.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
