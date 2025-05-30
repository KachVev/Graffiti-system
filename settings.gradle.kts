dependencyResolutionManagement {
    versionCatalogs {
        create("server") {
            from(files("${rootDir}/gradle/server.versions.toml"))
        }
        create("core") {
            from(files("${rootDir}/gradle/core.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "Graffiti-system"
include("arc-server")
