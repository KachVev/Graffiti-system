package arc.graffiti.api

import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.timer.SchedulerManager
import net.minestom.server.timer.TaskSchedule
import net.minestom.server.utils.time.TimeUnit
import org.slf4j.LoggerFactory

class ChunkManager(val manager: InstanceManager) {
    lateinit var container: InstanceContainer
    val logger = LoggerFactory.getLogger(javaClass)

    fun create(folder: String = "./world") {
        container = manager.createInstanceContainer()
        container.chunkLoader = AnvilLoader(folder)

        container.setGenerator { unit: GenerationUnit ->
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
        }
        container.setChunkSupplier(::LightingChunk)
    }

    fun setup(scheduler: SchedulerManager, shutdown: () -> Unit = {}) {
        scheduler.buildTask {
            val startTime = System.currentTimeMillis()
            logger.info("Saving chunks...")
            container.saveChunksToStorage()
            val endTime = System.currentTimeMillis() - startTime
            logger.info("Saved chunks in $endTime ms")
            TaskSchedule.minutes(5)
        }.repeat(5, TimeUnit.MINUTE).schedule()

        scheduler.buildShutdownTask {
            logger.info("Shutting down...")
            shutdown()
            container.saveChunksToStorage()
        }
    }
}