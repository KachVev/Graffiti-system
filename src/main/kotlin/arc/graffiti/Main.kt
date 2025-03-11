package arc.graffiti

import arc.graffiti.api.ChunkManager
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

    val graffiti = GraffitiHandler(worldInstance)

    MinecraftServer.getGlobalEventHandler().apply {
        addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            with(event) {
                spawningInstance = worldInstance
                player.respawnPoint = Pos(1.0, 42.0, 0.0)
            }
        }
        addListener(PlayerUseItemOnBlockEvent::class.java) { event ->
            graffiti.handle(event)
        }
    }

    minecraftServer.start("0.0.0.0", 25565)
}
