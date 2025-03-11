package arc.server.service

import arc.server.models.ResourcePack
import io.ktor.http.content.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.File

class ResourcePackService(val directory: File) {
    val resourcePacks = mutableListOf<ResourcePack>()

    fun saveResourcePack(key: String, fileItem: PartData.FileItem) {
        val resourcePackFile = File(directory,"$key.zip")
        if (resourcePackFile.exists()) {
            val isSuccess = resourcePackFile.delete()
            check(isSuccess) { "Failed to delete the existing resource pack file" }
        }

        fileItem.provider().toInputStream().use { input ->
            resourcePackFile.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }

        resourcePacks.add(ResourcePack(key, resourcePackFile))
    }


    fun exists(key: String): Boolean = resourcePacks.any { it.key == key }

    fun getResourcePack(key: String): File {
        return resourcePacks.first { it.key == key }.file
    }
}