plugins {
    id("java")
    id("java-library")
}

group = "com.github.dinomarlir"
version = "1.0-SNAPSHOT"

dependencies {
    api(libs.adventure.api)
    api(libs.adventure.text.minimessage)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}