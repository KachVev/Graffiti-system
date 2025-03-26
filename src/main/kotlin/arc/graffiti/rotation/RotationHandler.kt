package arc.graffiti.rotation

import net.minestom.server.utils.Direction

class RotationHandler {

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

    fun getDirection(blockFace: BlockFace): Direction {
        return blockFaceToDirection[blockFace] ?: Direction.NORTH
    }

    fun getYaw(direction: Direction): Float {
        return blockFaceToYaw[direction] ?: 0f
    }

    fun getPitch(direction: Direction): Float {
        return blockFaceToPitch[direction] ?: 0f
    }

    fun getGraffitiPosition(blockPos: Point, direction: Direction): Pos {
        return when (direction) {
            Direction.NORTH -> blockPos.add(0.5, 0.5, -0.01)
            Direction.SOUTH -> blockPos.add(0.5, 0.5, 1.01)
            Direction.EAST -> blockPos.add(1.01, 0.5, 0.5)
            Direction.WEST -> blockPos.add(-0.01, 0.5, 0.5)
            Direction.UP -> blockPos.add(0.5, 1.01, 0.5)
            Direction.DOWN -> blockPos.add(0.5, -0.01, 0.5)
        }.let { Pos(it.x(), it.y(), it.z()) }
    }

    fun getRotationFromBlockFace(blockFace: BlockFace): Rotation {
        val direction = getDirection(blockFace)
        return Rotation(getYaw(direction), getPitch(direction))
    }
}