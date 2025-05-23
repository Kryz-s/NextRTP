package ur.kryz.rtp.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.file.YamlFile
import ur.kryz.rtp.hook.PlaceholderAPIHook
import ur.kryz.rtp.parser.LegacyParser.parse
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object TitleAnimationManager {
    private val activeAnimations: MutableMap<UUID, ActiveAnimation> = HashMap()
    private val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private lateinit var plugin: NextRTPPlugin
    private var config: YamlFile? = null

    fun reload() {
        config!!.load()
    }

    fun init() {
        config = YamlFile.load("animations.yml", plugin)
        val instance = PlaceholderAPIHook.getInstance()
        scheduler.scheduleAtFixedRate({
            val now = System.currentTimeMillis()
            val toRemove: MutableList<UUID> = ArrayList()

            for ((uuid, anim) in activeAnimations) {
                val player = Bukkit.getPlayer(uuid)
                if (player == null || !player.isOnline) {
                    toRemove.add(uuid)
                    continue
                }

                val m = NextRTPPlugin.MINI_MESSAGE
                var updateTitle = false
                var updateSubtitle = false

                if (anim.title != null) {
                    if (now - anim.lastTitleUpdate >= anim.title.interval) {
                        val frame = anim.title.frames[anim.titleIndex]
                        anim.titleIndex = (anim.titleIndex + 1) % anim.title.frames.size
                        anim.lastTitleUpdate = now
                        anim.lastTitle =
                            m.deserialize(setPlaceholders(instance, player, frame))
                        updateTitle = true
                    }
                }

                if (anim.subTitle != null) {
                    if (now - anim.lastSubTitleUpdate >= anim.subTitle.interval) {
                        val frame = anim.subTitle.frames[anim.subTitleIndex]
                        anim.subTitleIndex = (anim.subTitleIndex + 1) % anim.subTitle.frames.size
                        anim.lastSubTitleUpdate = now
                        anim.lastSubtitle =
                            m.deserialize(setPlaceholders(instance, player, frame))
                        updateSubtitle = true
                    }
                }

                if (updateTitle || updateSubtitle) {
                    val interval =
                        if (anim.title != null) anim.title.interval else (if (anim.subTitle != null) anim.subTitle.interval else 1000)

                    val time = Title.Times.times(
                        Duration.ofMillis(0),
                        Duration.ofMillis((interval + 80).toLong()),
                        Duration.ofMillis(0)
                    )
                    val t = Title.title(anim.lastTitle, anim.lastSubtitle, time)
                    player.showTitle(t)
                }

                if (anim.actionBar != null) {
                    if (now - anim.lastActionBarUpdate >= anim.actionBar.interval) {
                        val frame = anim.actionBar.frames[anim.actionBarIndex]
                        anim.actionBarIndex = (anim.actionBarIndex + 1) % anim.actionBar.frames.size
                        anim.lastActionBarUpdate = now
                        player.sendActionBar(
                            m.deserialize(
                                setPlaceholders(instance, player, frame)
                            )
                        )
                    }
                }
            }
            for (uuid in toRemove) {
                activeAnimations.remove(uuid)
            }
        }, 0, 10, TimeUnit.MILLISECONDS)
    }

    fun set(pluginInstance: NextRTPPlugin) {
        plugin = pluginInstance
    }

    @JvmStatic
    fun start(player: Player, yaml: YamlFile) {
        if (!yaml.get("titles.enabled", false)) return
        if (!yaml.get("titles.delay.ui.enabled", false)) return

        val path = yaml.getSection("titles.delay.ui")
            ?: throw NoSuchElementException("The path 'titles.delay.ui' was not found, please add it.")
        val delay = yaml.get("teleport.delay", 5)

        val titleValue = path.get("title", "")
        val subtitleValue = path.get("subtitle", "")
        val actionbarValue = path.get("actionbar", "")

        val title = loadAnimation(
            titleValue,
            delay
        ) { frames: List<String>, interval: Int ->
            TitleAnimation(
                frames,
                interval
            )
        }
        val subTitle = loadAnimation(
            subtitleValue,
            delay
        ) { frames: List<String>, interval: Int ->
            SubTitleAnimation(
                frames,
                interval
            )
        }
        val actionBar = loadAnimation(
            actionbarValue,
            delay
        ) { frames: List<String>, interval: Int ->
            ActionBarAnimation(
                frames,
                interval
            )
        }

        activeAnimations[player.uniqueId] = ActiveAnimation(
            title, subTitle, actionBar
        )
    }

    private fun <T> loadAnimation(pathName: String, delay: Int, factory: AnimationFactory<T>): T {
        if (config!!.getSection("animations.$pathName") == null || pathName.isBlank()) {
            return factory.create(listOf(parse(pathName)), delay * 1000)
        }

        val interval = config!!.get<Int>("animations.$pathName.interval")
        val rawFrames = config!!.get<List<String>>("animations.$pathName.value")!!
        val parsedFrames: MutableList<String> = ArrayList()
        for (frame in rawFrames) {
            parsedFrames.add(parse(frame))
        }

        return factory.create(parsedFrames, interval!!)
    }

    @JvmStatic
    fun stop(player: Player) {
        activeAnimations.remove(player.uniqueId)
    }

    fun isAnimating(player: Player): Boolean {
        return activeAnimations.containsKey(player.uniqueId)
    }

    private fun setPlaceholders(ins: PlaceholderAPIHook, player: Player, frame: String):String {
        return ins.setPlaceholders(player, frame)
    }

    class ActiveAnimation(
        val title: TitleAnimation?,
        val subTitle: SubTitleAnimation?,
        val actionBar: ActionBarAnimation?
    ) {
        var lastTitleUpdate: Long = 0L
        var lastSubTitleUpdate: Long = 0L
        var lastActionBarUpdate: Long = 0L
        var titleIndex: Int = 0
        var subTitleIndex: Int = 0
        var actionBarIndex: Int = 0
        var lastTitle: Component = Component.empty()
        var lastSubtitle: Component = Component.empty()
    }

    @JvmRecord
    data class TitleAnimation(val frames: List<String>, val interval: Int)

    @JvmRecord
    data class SubTitleAnimation(val frames: List<String>, val interval: Int)

    @JvmRecord
    data class ActionBarAnimation(val frames: List<String>, val interval: Int)

    private fun interface AnimationFactory<T> {
        fun create(frames: List<String>, interval: Int): T
    }
}
