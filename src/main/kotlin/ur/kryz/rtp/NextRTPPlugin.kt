package ur.kryz.rtp

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import ur.kryz.rtp.command.paper.NextRootCommand
import ur.kryz.rtp.command.paper.NextRootCommand.add
import ur.kryz.rtp.command.subcommand.ReloadCommand
import ur.kryz.rtp.file.YamlFile
import ur.kryz.rtp.hook.PlaceholderAPIHook
import ur.kryz.rtp.hook.VaultHook
import ur.kryz.rtp.manager.TitleAnimationManager
import ur.kryz.rtp.modules.ModuleManager
import ur.kryz.rtp.utils.PlayerUtil

class NextRTPPlugin : JavaPlugin() {
    private lateinit var moduleManager: ModuleManager
    private var lang: YamlFile? = null

    override fun onEnable() {
        plugin = this
        saveDefaultConfig()

        pluginName = name

        this.setupHooks()

        lang = YamlFile.load("lang/lang.yml", this)
//        set(lang!!)
        PlayerUtil.set(lang!!)

        TitleAnimationManager.set(this)

        moduleManager = ModuleManager(this)
        moduleManager.loadModules()

        TitleAnimationManager.init()

        setCommands()
    }

    override fun onDisable() {
        moduleManager.disableModules()
    }

    private fun setCommands() {
        add(ReloadCommand(this))
        this.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { NextRootCommand.registrar(it) }
    }

    fun consoleMessage(log: String) {
        val message = prefix.append(MINI_MESSAGE.deserialize("<reset>$log").color(NamedTextColor.WHITE))
        Bukkit.getConsoleSender().sendMessage(message)
    }

    fun reload() {
        reloadConfig()
        lang!!.load()
        TitleAnimationManager.reload()

        for (module in moduleManager.getModules()) {
            module.reload()
        }
    }

    fun getModuleManager(): ModuleManager {
        return moduleManager
    }

    private fun setupHooks() {
        VaultHook(this)
        PlaceholderAPIHook(this).register()
    }

    companion object {
        @JvmField
        val MINI_MESSAGE: MiniMessage = MiniMessage.builder()
            .tags(StandardTags.defaults())
            .build()
        private var pluginName = ""
        private var plugin: JavaPlugin? = null

        @JvmStatic
        fun runTask(runnable: Runnable?) {
            Bukkit.getScheduler().runTask(plugin!!, runnable!!)
        }

        private val prefix: Component
            get() = MINI_MESSAGE.deserialize("<color:#109ded>[$pluginName] </color>")
    }
}
