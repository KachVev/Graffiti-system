package arc.graffiti

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.ItemDisplayMeta
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.BlockFace
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.utils.Direction

class GraffitiHandler(val worldInstance: Instance) {
    val blockFaceToDirection = mapOf(
        BlockFace.NORTH to Direction.NORTH,
        BlockFace.SOUTH to Direction.SOUTH,
        BlockFace.EAST to Direction.EAST,
        BlockFace.WEST to Direction.WEST,
        BlockFace.TOP to Direction.UP,
        BlockFace.BOTTOM to Direction.DOWN
    )

    val blockFaceToYaw = mapOf(
        Direction.NORTH to 0f,
        Direction.SOUTH to 180f,
        Direction.EAST to 90f,
        Direction.WEST to -90f
    )

    val blockFaceToPitch = mapOf(
        Direction.UP to -90f,
        Direction.DOWN to 90f
    )

    fun handle(event: PlayerUseItemOnBlockEvent) {
        val player = event.player
        if (player.itemInMainHand.material() != Material.STICK) return

        val targetBlock = event.position
        val hitDirection = blockFaceToDirection[event.blockFace] ?: return
        val graffiti = createGraffiti()
        graffiti.setInstance(worldInstance, getGraffitiPosition(targetBlock, hitDirection))

        graffiti.setView(blockFaceToYaw[hitDirection] ?: 0f, blockFaceToPitch[hitDirection] ?: 0f)
        player.sendMessage("Граффити создано на ${targetBlock.x()}, ${targetBlock.y()}, ${targetBlock.z()}")
    }

    fun createGraffiti() = Entity(EntityType.ITEM_DISPLAY).apply {
        val displayMeta = entityMeta as ItemDisplayMeta
        displayMeta.itemStack = ItemStack.of(Material.PAINTING).withCustomModelData(
            emptyList<Float>(),
            emptyList<Boolean>(),
            listOf("graffiti1"),
            emptyList()
        )
        displayMeta.isHasNoGravity = true
    }

    fun getGraffitiPosition(blockPos: Point, direction: Direction) = when (direction) {
        Direction.NORTH -> blockPos.add(0.5, 0.5, -0.01)
        Direction.SOUTH -> blockPos.add(0.5, 0.5, 1.01)
        Direction.EAST -> blockPos.add(1.01, 0.5, 0.5)
        Direction.WEST -> blockPos.add(-0.01, 0.5, 0.5)
        Direction.UP -> blockPos.add(0.5, 1.01, 0.5)
        Direction.DOWN -> blockPos.add(0.5, -0.01, 0.5)
    }.let { Pos(it.x(), it.y(), it.z()) }
}