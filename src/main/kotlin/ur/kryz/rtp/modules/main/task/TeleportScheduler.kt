package ur.kryz.rtp.modules.main.task

import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import ur.kryz.rtp.NextRTPPlugin.Companion.runTask
import ur.kryz.rtp.animation.ParticleProvider.get
import ur.kryz.rtp.api.events.RandomTeleportEndEvent
import ur.kryz.rtp.manager.TitleAnimationManager.start
import ur.kryz.rtp.manager.TitleAnimationManager.stop
import ur.kryz.rtp.modules.main.RandomTeleportModule
import ur.kryz.rtp.modules.main.RandomTeleportProcessor.random
import ur.kryz.rtp.parser.LegacyParser.parse
import ur.kryz.rtp.utils.DateFormatUtil.formatTime
import ur.kryz.rtp.utils.PlayerUtil.sendParsed
import ur.kryz.rtp.utils.RTPUtils.processSuccess
import java.util.*
import kotlin.math.max

class TeleportScheduler(private val module: RandomTeleportModule) : Scheduler {
    private val showDelayMessages: Boolean

    val isShowSuccessMessage: Boolean
    val isShowTitles: Boolean

    val title: String

    val isShowSuccessTitles: Boolean

    val subtitle: String
    val actionbar: String
    private val teleportMoney: Number

    private var delaySound: Sound? = null
    var successSound: Sound? = null
        private set

    val isSendSound: Boolean
    private val sendDelaySound: Boolean

    val isSendSuccessSound: Boolean

    init {
        val file = module.config

        this.showDelayMessages = file.get("titles.delay.message", false)
        this.isShowSuccessMessage = file.get("titles.success.message", true)
        this.isShowTitles = file.get("titles.enabled", false)

        this.teleportMoney = file.get("teleport.money", 100.0)
        this.isShowSuccessTitles = file.get("titles.success.ui.enabled", false)
        this.title = parse(file.get("titles.success.ui.title", ""))
        this.subtitle = parse(file.get("titles.success.ui.subtitle", ""))
        this.actionbar = parse(file.get("titles.success.ui.actionbar", ""))

        this.isSendSound = file.get("sounds.enabled", false)
        this.sendDelaySound = file.get("sounds.delay.enabled", false)
        this.isSendSuccessSound = file.get("sounds.success.enabled", false)

        loadSounds()
    }

    override fun tick() {
        val iterator: MutableIterator<Map.Entry<Player, RtpContainer>> = QUEUE.entries.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            val player = entry.key
            val container = entry.value

            if (!container.isEquals(player.location)) {
                sendParsed(player, "messages.teleport.cancelled")
                stop(player)
                module.particleAnimationManager.stopFor(player)
                iterator.remove()
                continue
            }

            if (container.teleporting) continue

            val tickCounter = container.tickCounter
            val maxTicks = container.time * 20

            if (tickCounter % 20 == 0 && tickCounter < maxTicks) {
                container.seconds = max(0.0, (container.time - (tickCounter / 20)).toDouble())
                    .toInt()
                runTask {
                    if (sendDelaySound) {
                        player.playSound(delaySound!!)
                    }
                    if(showDelayMessages) {
                        sendParsed(
                            player, "messages.teleport.delay",
                            Placeholder.unparsed(
                                "time", formatTime(
                                    container.seconds
                                )
                            )
                        )
                    }
                }
            }

            if (tickCounter >= maxTicks) {
                container.teleporting = true
                this.cancel(player)

                teleportNow(player, container.world)
                continue
            }

            container.tickCounter = tickCounter + 5
        }
    }

    private fun teleportNow(player: Player, world: World) {
        val event = RandomTeleportEndEvent(player)
        val pm = Bukkit.getPluginManager()
        player.random(world).thenAccept { success: Boolean ->
            runTask {
                if (success) {
                    event.isFailed = false
                    processSuccess(module.cooldownManager, this, player, teleportMoney.toDouble())
                    pm.callEvent(event)
                } else {
                    event.isFailed = true
                    sendParsed(player, "messages.teleport.failed")
                    pm.callEvent(event)
                }
            }
        }
    }

    fun queue(player: Player, time: Int, world: World) {
        sendParsed(player, "messages.teleport.start")

        try {
            val animName = module.config.get<String>("teleport.particle_animation")!!.uppercase(Locale.getDefault())
            //            AnimationType animation = AnimationType.valueOf(animName);
            module.particleAnimationManager.startFor(player, get(animName))
            QUEUE[player] = RtpContainer(time, player.location, world, false, 0)
            start(player, module.config)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isQueued(player: Player?): Boolean {
        return QUEUE.containsKey(player)
    }

    fun cancel(player: Player?) {
        val container = QUEUE.remove(player)
        if (container != null) {
            stop(player!!)
            module.particleAnimationManager.stopFor(player)
        }
    }

    val queue: Map<Player, RtpContainer>
        get() = QUEUE

    fun loadSounds() {
        val section = module.config.getSection("sounds")
            ?: throw NoSuchElementException("Sounds key not found, please add it into rtp.yml")

        val delay = section.getSection("delay")
        if(delay != null) {
            val v1: Number = delay.get("volume", 1.0f)
            val p1: Number = delay.get("pitch", 1.0f)
            val sound1 = delay.get("sound", "BLOCK_NOTE_BLOCK_HAT")
            val s1 = org.bukkit.Sound.valueOf(sound1)

            delaySound = Sound.sound(
                s1.key(),
                Sound.Source.MASTER,
                v1.toFloat(),
                p1.toFloat()
            )
        }

        val success = section.getSection("success")
        if(success != null) {
            val v2: Number = success.get("volume", 1.0f)
            val p2: Number = success.get("pitch", 1.0f)
            val sound2 = success.get("sound", "ENTITY_ENDERMAN_TELEPORT")
            val s2 = org.bukkit.Sound.valueOf(sound2)

            successSound = Sound.sound(
                s2.key(),
                Sound.Source.MASTER,
                v2.toFloat(),
                p2.toFloat()
            )
        }
    }

    companion object {
        private val QUEUE: MutableMap<Player, RtpContainer> = HashMap()
    }
}
