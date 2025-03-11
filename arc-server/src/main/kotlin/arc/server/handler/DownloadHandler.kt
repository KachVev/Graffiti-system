package arc.server.handler

import arc.server.server.ResourcePackService
import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

class DownloadHandler(val resourcePackService: ResourcePackService) {
    fun Route.downloadRoute() {
        get("/download") { handleDownload(call) }
    }

    private suspend fun handleDownload(call: ApplicationCall) {
        if (!resourcePackService.exists()) {
            call.respond(HttpStatusCode.NotFound, "No resourcepack uploaded yet")
            return
        }

        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Attachment.withParameter(
                ContentDisposition.Parameters.FileName, "resourcepack.zip"
            ).toString()
        )
        call.respondFile(resourcePackService.resourcePackFile)
    }
}