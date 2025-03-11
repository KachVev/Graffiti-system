plugins {
    alias(server.plugins.serialization)
    alias(server.plugins.shadow)
    application
}


dependencies {
    implementation(server.ktor.server.core)
    implementation(server.ktor.server.netty)
    implementation(server.ktor.server.request.validation)
    implementation(server.ktor.server.content.negotiation)
    implementation(server.ktor.server.cio)
    implementation(server.ktor.serialization.kotlinx.json)
    implementation(server.ktor.serialization.jackson)
    implementation(server.kotlinx.serialization)
}


application {
    mainClass.set("arc.server.MainKt")
}

tasks.shadowJar {
    archiveBaseName.set("arc-server")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "arc.server.MainKt"
    }
}

