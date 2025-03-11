plugins {
    alias(core.plugins.kotlin)
}

group = "arc.graffiti"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(core.minestom)
    implementation(core.ktor.core)
    implementation(core.ktor.serialization)
    implementation(core.ktor.content)
    implementation(core.ktor.okhttp)
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")


    repositories {
        mavenCentral()
    }
}

kotlin {
    jvmToolchain(23)
}