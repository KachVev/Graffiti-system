package arc.graffiti.model

import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.Instance

data class Location(
    val instance: Instance,
    val x: Double,
    val y: Double,
    val z: Double,
) {
    val position get() = Pos(x, y, z)
}