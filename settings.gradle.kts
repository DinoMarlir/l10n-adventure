rootProject.name = "l10n-adventure"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs.create("libs") {
        version("adventure", "5.1.1")
        version("junit", "5.10.2")
        version("junitPlatform", "1.10.2")

        library("adventure-api", "net.kyori", "adventure-api").versionRef("adventure")
        library("adventure-text-minimessage", "net.kyori", "adventure-text-minimessage").versionRef("adventure")

        library("junit-bom", "org.junit", "junit-bom").versionRef("junit")
        library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
        library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
        library("junit-platform-launcher", "org.junit.platform", "junit-platform-launcher").versionRef("junitPlatform")
    }
}