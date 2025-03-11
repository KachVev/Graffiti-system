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

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}