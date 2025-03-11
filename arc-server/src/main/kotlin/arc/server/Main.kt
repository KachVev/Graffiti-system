package arc.server

import arc.server.handler.DownloadHandler
import arc.server.handler.UploadHandler
import arc.server.service.ResourcePackService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import java.io.File

val directory = File("/resourcepacks")

fun Application.setup() {
    install(ContentNegotiation) {
        json()
    }

    val allowedIps = listOf("0:0:0:0:0:0:0:1", "127.0.0.1", "85.254.73.222")
    val resourcePackService = ResourcePackService(directory)

    routing {
        UploadHandler(resourcePackService, allowedIps).apply { uploadRoute() }
        DownloadHandler(resourcePackService).apply { downloadRoute() }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::setup)
        .start(wait = true)
}
