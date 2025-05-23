package ur.kryz.rtp.modules.main.task

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import ur.kryz.rtp.NextRTPPlugin.Companion.runTask
import ur.kryz.rtp.api.events.RandomTeleportEndEvent
import ur.kryz.rtp.api.events.RandomTeleportStartEvent
import ur.kryz.rtp.modules.main.RandomTeleportModule
import ur.kryz.rtp.modules.main.RandomTeleportProcessor.random
import ur.kryz.rtp.utils.PlayerUtil.sendParsed
import ur.kryz.rtp.utils.RTPUtils.processSuccess
import java.util.concurrent.CompletableFuture

class RtpTask(private val module: RandomTeleportModule) : BukkitRunnable() {
    private val file = module.config
    private lateinit var scheduler: TeleportScheduler

    init {
        this.set(TeleportScheduler(module))
    }

    override fun run() {
        scheduler.tick()
    }

    fun queue(player: Player, delay: Int, world: World) {
        val event = RandomTeleportStartEvent(player)
        val vo = Bukkit.getPluginManager()
        if (player.hasPermission("nextrtp.bypass.timer")) {
            val e = RandomTeleportEndEvent(player)
            player.random(world).thenAccept { success: Boolean ->
                runTask {
                    if (success) {
                        e.isFailed = false
                        processSuccess(
                            module.cooldownManager,
                            scheduler,
                            player,
                            file.get<Number>("teleport.money")!!
                        )
                        vo.callEvent(e)
                    } else {
                        e.isFailed = true
                        sendParsed(player, "messages.teleport.failed")
                        vo.callEvent(e)
                    }
                }
            }
            return
        }
        CompletableFuture.runAsync {
            scheduler.queue(player, delay, world)
            vo.callEvent(event)
        }
    }

    fun isQueued(player: Player?): Boolean {
        return scheduler.isQueued(player)
    }

    fun getContainer(player: Player?): RtpContainer? {
        return scheduler.queue[player]
    }

    fun cancel(player: Player?) {
        scheduler.cancel(player)
    }

    fun set(scheduler: TeleportScheduler) {
        this.scheduler = scheduler
    }

    val playerQueue: Map<Player, RtpContainer>
        get() = scheduler.queue

    fun loadSounds() {
        scheduler.loadSounds()
    }
}
