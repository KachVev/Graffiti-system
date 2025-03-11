plugins {
    kotlin("jvm") version "2.1.10"
}

group = "arc.graffiti"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:1_21_4-44b34717ed")
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")


    repositories {
        mavenCentral()
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}