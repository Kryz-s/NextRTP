package ur.kryz.rtp.modules.main

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent
import ur.kryz.rtp.adapter.BiomeAdapter
import ur.kryz.rtp.file.YamlFile
import ur.kryz.rtp.file.YamlSection
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

object RandomTeleportProcessor {

    private val playerRandomTeleports = ConcurrentHashMap<UUID, Boolean>()
    private val random: Random = Random()
    private lateinit var file: YamlFile
    private val blacklistBlocks: MutableSet<Material> = HashSet()
    private val blacklistBiomes: MutableSet<String> = HashSet()

    @JvmStatic
    fun Player.random(world: World): CompletableFuture<Boolean> {
        playerRandomTeleports[this.uniqueId] = true
        val attempts: Int = file.get("teleport.attempts", 10)
        val section = file.getSection("teleport.area.per_world.${world.name}")
        val (centerX, centerZ, radius, maxY) = resolveTeleportArea(section ?: file.getSection("teleport.area") ?: error("teleport.area key not found in rtp.yml"), world)
        return tryTeleport(this, world, maxY, centerX, centerZ, radius, attempts)
    }

    private data class TeleportArea(val centerX: Double, val centerZ: Double, val radius: Int, val maxY: Int)

    private fun resolveTeleportArea(section: YamlSection, world: World): TeleportArea {
        val useWorldBorder = section.get("use_world_border", false)
        val maxY: Int = section.get("max_y", 100)
        val radius = section.get("radius", 100)

        val (centerX, centerZ) = if (useWorldBorder) {
            val center = world.worldBorder.center
            center.x to center.z
        } else {
            section.get<Double>("center_x") to section.get<Double>("center_z")
        }

        return TeleportArea(centerX!!, centerZ!!, radius, maxY)
    }

    private fun tryTeleport(
        player: Player,
        world: World,
        maxY: Int,
        baseX: Double,
        baseZ: Double,
        radius: Int,
        attempts: Int
    ): CompletableFuture<Boolean> {
        val candidateLocations = mutableListOf<Location>()

        repeat(attempts) {
            val blockX = random.nextInt((baseX - radius).toInt(), (baseX + radius).toInt())
            val blockZ = random.nextInt((baseZ - radius).toInt(), (baseZ + radius).toInt())
            val safeY = getSafeY(world, blockX, blockZ, maxY)
            candidateLocations.add(Location(world, blockX + 0.5, safeY, blockZ + 0.5))
        }

        val validLocation = findValidTeleportLocation(candidateLocations)
        return if (validLocation != null) {
            teleport(player, validLocation)
        } else {
            CompletableFuture.completedFuture(false)
        }
    }

    private fun findValidTeleportLocation(locations: MutableList<Location>): Location? {
        return locations.firstOrNull { loc ->
            val material = loc.block.type
            val key: NamespacedKey? = BiomeAdapter.instance.getKey(loc.block.biome)
            var stringKey = ""
            if(key != null) {
                stringKey = key.key
            }
            val belowMaterial = loc.clone().add(0.0, -1.0, 0.0).block.type

            material !in blacklistBlocks &&
                    belowMaterial !in blacklistBlocks &&
                    stringKey !in blacklistBiomes
        }
    }

    private fun teleport(player: Player, location: Location): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()

        player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN)
            .whenComplete { success, throwable ->
                if (throwable != null) {
                    future.completeExceptionally(throwable)
                } else {
                    if (success) {
                        playerRandomTeleports.remove(player.uniqueId)
                    }
                    future.complete(success)
                }
            }

        return future
    }

    private fun getSafeY(world: World, x: Int, z: Int, maxY: Int): Double {
        val minY = world.minHeight

        for (y in maxY downTo minY) {
            val loc = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
//            if(loc.block.type !in materials) {
                if (loc.block.type == Material.WATER) {
                    return y.toDouble()
                } else if (loc.block.type.isSolid) {
                    return (y + 1).toDouble()
                }
//            }
        }

        return (world.getHighestBlockYAt(x, z) + 1).toDouble()
    }

    fun set(file: YamlFile) {
        RandomTeleportProcessor.file = file
        load()
    }

    private fun load() {
        blacklistBlocks.addAll(file
            .get<List<String>>("teleport.blacklisted_blocks", listOf())
            .map(Material::valueOf)
            .toSet())
        blacklistBiomes.addAll(file
            .get<List<String>>("teleport.blacklisted_biomes", listOf())
            .toSet())
    }

    fun reload() {
        load()
    }
}
