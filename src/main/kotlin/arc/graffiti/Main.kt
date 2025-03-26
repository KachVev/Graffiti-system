package arc.graffiti

import arc.graffiti.api.ChunkManager
import arc.graffiti.models.Location
import arc.graffiti.resourcepack.ResourcePackProvider
import kotlinx.coroutines.runBlocking
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent


fun main() {
    val minecraftServer = MinecraftServer.init()
    val instanceManager = MinecraftServer.getInstanceManager()
    val chunkGenerator = ChunkManager(instanceManager)
    chunkGenerator.create()
    val worldInstance = chunkGenerator.container
    val spawner = GraffitiSpawner()

    val resourcePackProvider = ResourcePackProvider("http://localhost:8080", "graffiti")

    MinecraftServer.getGlobalEventHandler().apply {
        addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            with(event) {
                spawningInstance = worldInstance
                player.respawnPoint = Pos(1.0, 42.0, 0.0)
                runBlocking {
                    resourcePackProvider.sendResourcePack(player)
                }
            }
        }
        addListener(PlayerUseItemOnBlockEvent::class.java) { event ->
           spawner.handle(event)
        }
    }

    minecraftServer.start("0.0.0.0", 25565)
}
