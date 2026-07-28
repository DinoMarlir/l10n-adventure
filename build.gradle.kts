plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "io.github.dinomarlir"
version = "1.0-SNAPSHOT"

java {
    withSourcesJar()
    withJavadocJar()
}

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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("l10n-adventure")
                description.set("A small, platform-independent localization / i18n library for Minecraft-based applications.")
            }
        }
    }
}
