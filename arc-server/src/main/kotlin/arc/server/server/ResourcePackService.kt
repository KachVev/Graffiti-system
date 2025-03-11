package arc.server.server

import io.ktor.http.content.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.File

class ResourcePackService(val resourcePackFile: File) {

    fun saveResourcePack(fileItem: PartData.FileItem) {
        if (resourcePackFile.exists()) {
            val isSuccess = resourcePackFile.delete()
            check(isSuccess) { "Failed to delete the existing resource pack file" }
        }

        fileItem.provider().toInputStream().use { input ->
            resourcePackFile.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
    }

    fun exists(): Boolean = resourcePackFile.exists()
}