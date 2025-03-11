package arc.server.handler

import arc.server.service.ResourcePackService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class UploadHandler(
    val resourcePackService: ResourcePackService,
    val allowedIps: List<String>
) {
    fun Route.uploadRoute() {
        post("/upload") { handleUpload(call) }
    }

    suspend fun handleUpload(call: ApplicationCall) {
        val clientIp = call.request.origin.remoteAddress
        println("Client IP: $clientIp, Allowed: $allowedIps")

        if (clientIp !in allowedIps) {
            call.respond(HttpStatusCode.Companion.Forbidden, "Access denied for IP: $clientIp")
            return
        }

        val key = call.parameters["key"]
        if (key == null) {
            call.respond(HttpStatusCode.Forbidden, "Invalid or missing upload key")
            return
        }

        val savedFile = processUploadedFile(call, key)

        if (savedFile) {
            call.respond(HttpStatusCode.Companion.OK, "Resourcepack uploaded successfully")
        } else {
            call.respond(HttpStatusCode.Companion.BadRequest, "No file uploaded!")
        }
    }

    suspend fun processUploadedFile(call: ApplicationCall, key: String): Boolean {
        val multipart = call.receiveMultipart()
        var isSaved = false

        multipart.forEachPart { part ->
            try {
                if (part is PartData.FileItem) {
                    println("Processing file: ${part.originalFileName}")
                    resourcePackService.saveResourcePack(key, part)
                    isSaved = true
                    println("Received file: ${part.originalFileName}")
                }
            } finally {
                part.dispose()
            }
        }
        return isSaved
    }
}