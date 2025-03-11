package arc.server.service

import arc.server.models.ResourcePack
import io.ktor.http.content.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.File

class ResourcePackService(val directory: File) {
    val resourcePacks = mutableMapOf<String, ResourcePack>()

    init {
        loadExistingResourcePacks()
    }

    fun loadExistingResourcePacks() {
        if (!directory.exists()) {
            directory.mkdirs()
        }

        directory.listFiles()?.forEach { keyDir ->
            if (keyDir.isDirectory) {
                val resourcePackFile = File(keyDir, "pack.zip")
                if (resourcePackFile.exists()) {
                    resourcePacks[keyDir.name] = ResourcePack(keyDir.name, resourcePackFile)
                }
            }
        }
    }

    fun saveResourcePack(key: String, fileItem: PartData.FileItem) {
        val keyDirectory = File(directory, key)
        if (!keyDirectory.exists()) {
            keyDirectory.mkdirs()
        }

        val resourcePackFile = File(keyDirectory, "pack.zip")
        if (resourcePackFile.exists()) {
            val isSuccess = resourcePackFile.delete()
            check(isSuccess) { "Failed to delete the existing resource pack file" }
        }

        fileItem.provider().toInputStream().use { input ->
            resourcePackFile.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }

        resourcePacks[key] = ResourcePack(key, resourcePackFile)
    }

    fun exists(key: String): Boolean = resourcePacks.containsKey(key)

    fun getResourcePack(key: String): File {
        return resourcePacks[key]?.file ?: throw NoSuchElementException("Resource pack not found for key: $key")
    }
}
