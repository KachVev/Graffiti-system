package arc.graffiti

import net.minestom.server.event.player.PlayerUseItemOnBlockEvent
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType

class GraffitiSpawner(val worldInstance: Instance, val rotationHandler: RotationHandler) {

    fun handle(event: PlayerUseItemOnBlockEvent) {
        val player = event.player
        val targetBlock = event.position
        val hitDirection = event.blockFace

        val graffiti = Graffiti("graffiti1")

        val graffitiPosition = rotationHandler.getGraffitiPosition(targetBlock, rotationHandler.getDirection(hitDirection))
        val graffitiRotation = rotationHandler.getRotationFromBlockFace(hitDirection)

        graffiti.spawn(Location(worldInstance, graffitiPosition), graffitiRotation)

        player.sendMessage("Граффити создано на ${targetBlock.x()}, ${targetBlock.y()}, ${targetBlock.z()}")
    }
}
