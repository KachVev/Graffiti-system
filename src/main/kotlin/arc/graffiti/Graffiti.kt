package arc.graffiti

import arc.graffiti.models.Location
import arc.graffiti.models.Rotation
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.ItemDisplayMeta
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

@Suppress("unused")
class Graffiti(val modelData: String) {

    val entity: Entity = Entity(EntityType.ITEM_DISPLAY).apply {
        val displayMeta = entityMeta as ItemDisplayMeta
        displayMeta.itemStack = ItemStack.of(Material.PAINTING).withCustomModelData(
            emptyList<Float>(),
            emptyList<Boolean>(),
            listOf(modelData),
            emptyList()
        )
        displayMeta.isHasNoGravity = true
    }

    fun spawn(location: Location, rotation: Rotation = Rotation()) {
        entity.setInstance(location.instance, location.position)
        entity.setView(rotation.yaw, rotation.pitch)
    }

    fun remove() {
        entity.remove()
    }
}