package ur.kryz.rtp.hook

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.modules.main.RandomTeleportModule

class PlaceholderAPIHook(private val plugin: NextRTPPlugin) {
    private var enabled = false
    private var expansion: Expansion? = null

    init {
        instance = this
        enabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
        if (enabled) {
            expansion = Expansion(plugin)
        }
    }

    fun register() {
        expansion?.register()
    }

    fun setPlaceholders(player: Player, msg: String): String {
        var msg = msg
        if (enabled) {
            msg = PlaceholderAPI.setBracketPlaceholders(player, msg)
            return PlaceholderAPI.setPlaceholders(player, msg)
        }
        return msg
    }

    class Expansion(private val plugin: NextRTPPlugin) : PlaceholderExpansion() {
        override fun getIdentifier(): String {
            return "nextrtp"
        }

        override fun getAuthor(): String {
            return plugin.pluginMeta.authors.first()
        }

        override fun getVersion(): String {
            return plugin.pluginMeta.version
        }

        override fun getPlaceholders(): List<String> {
            return listOf(
                "teleport_progress_time",
                "teleport_cooldown",
                "teleport_money"
            )
        }

        override fun onRequest(player: OfflinePlayer, params: String): String {
            if (player !is Player) return ""
            val module = plugin.getModuleManager().getModule(
                RandomTeleportModule::class.java
            )
            return when (params) {
                "teleport_progress_time" -> {
                    val container = module.task.getContainer(player)
                    if (container == null) module.config.get<String>("teleport.delay")
                    val seconds = container!!.seconds
                    seconds.toString()
                }

                "teleport_cooldown" -> {
                    val cooldownManager = module.cooldownManager
                    if (!cooldownManager.isOnCooldown(player)) module.config.get<String>("teleport.cooldown")
                    val left = cooldownManager.getTimeLeft(player) / 1000L
                    left.toString()
                }

                "teleport_money" -> {
                    val money = module.config.get<Number>("teleport.money")
                    money!!.toDouble().toString()
                }

                else -> ""
            }
        }
    }

    companion object {
        private lateinit var instance: PlaceholderAPIHook

        @JvmStatic
        fun getInstance() = instance
    }
}