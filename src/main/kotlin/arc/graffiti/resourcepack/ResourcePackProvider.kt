package arc.graffiti.resourcepack

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.minestom.server.entity.Player
import java.io.File
import java.util.*

class ResourcePackProvider(val serverUrl: String, val key: String) {
    val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun updateResourcePack(resourcePack: File, key: String) {
        if (!resourcePack.exists()) {
            println("ResourcePack file does not exist: ${resourcePack.absolutePath}")
            return
        }

        val response = httpClient.submitFormWithBinaryData(
            url = "$serverUrl/upload",
            formData = formData {
                append("key", key)
                append("file", resourcePack.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "application/zip")
                    append(HttpHeaders.ContentDisposition, "filename=${resourcePack.name}")
                })
            }
        )

        println("Response: ${response.status}")
        if (response.status != HttpStatusCode.OK) {
            println("Error: ${response.bodyAsText()}")
        }
    }

    suspend fun sendResourcePack(player: Player) {
        val hash = getResourcePackHash()
        val resourcePackInfo = ResourcePackInfo.resourcePackInfo(
            UUID.randomUUID(),
            Url("$serverUrl/download?key=$key").toURI(),
            hash
        )

        val request = ResourcePackRequest.resourcePackRequest()
            .packs(resourcePackInfo)
            .replace(true)
            .required(true)
            .build()

        player.sendResourcePacks(request)
    }

    suspend fun getResourcePackHash(): String {
        val response = httpClient.get("$serverUrl/resourcepacks/download?key=$key")
        if (response.status != HttpStatusCode.OK) {
            println("Failed to download resource pack for hashing: ${response.status}")
            return ""
        }

        val bytes = response.bodyAsChannel().toByteArray()
        return sha1Base64(bytes)
    }
}
