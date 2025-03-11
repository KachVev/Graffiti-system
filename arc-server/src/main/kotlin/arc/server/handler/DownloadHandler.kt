package arc.server.handler

import arc.server.service.ResourcePackService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class DownloadHandler(val resourcePackService: ResourcePackService) {
    fun Route.downloadRoute() {
        get("/download") { handleDownload(call) }
    }

    suspend fun handleDownload(call: ApplicationCall) {
        val key = call.parameters["key"]
        if (key == null) {
            call.respond(HttpStatusCode.Forbidden, "Invalid or missing key")
            return
        }

        if (!resourcePackService.exists(key)) {
            call.respond(HttpStatusCode.NotFound, "No resourcepack uploaded yet for key: $key")
            return
        }

        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Attachment.withParameter(
                ContentDisposition.Parameters.FileName, "$key.zip"
            ).toString()
        )
        call.respondFile(resourcePackService.getResourcePack(key))
    }
}