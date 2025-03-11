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