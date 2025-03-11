package arc.server.handler

import arc.server.server.ResourcePackService
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

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


        val savedFile = processUploadedFile(call)

        if (savedFile) {
            call.respond(HttpStatusCode.Companion.OK, "Resourcepack uploaded successfully")
        } else {
            call.respond(HttpStatusCode.Companion.BadRequest, "No file uploaded!")
        }
    }

    suspend fun processUploadedFile(call: ApplicationCall): Boolean {
        val multipart = call.receiveMultipart()
        var isSaved = false

        multipart.forEachPart { part ->
            try {
                if (part is PartData.FileItem) {
                    println("Processing file: ${part.originalFileName}")
                    resourcePackService.saveResourcePack(part)
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