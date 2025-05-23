package ur.kryz.rtp.hook

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit.getServer
import ur.kryz.rtp.NextRTPPlugin

class VaultHook(private val plugin: NextRTPPlugin) {
    private var _economy: Economy? = null
    private var enabled: Boolean = false

    companion object {
        private lateinit var INSTANCE: VaultHook

        @JvmStatic
        fun getInstance() : VaultHook = INSTANCE
    }

    val economy: Economy
        get() = _economy!!

    init {
        load()
        INSTANCE = this
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    private fun load() : Boolean {
        if (getServer().pluginManager.getPlugin("Vault") == null) {
            plugin.consoleMessage("The <gold>Vault</gold> dependency has not been found.")
            return false;
        }
        if(!getServer().pluginManager.isPluginEnabled("Vault")) {
            plugin.consoleMessage("The <gold>Vault</gold> dependency is not enabled.")
            return false;
        }
        val rsp = getServer().servicesManager.getRegistration(
            Economy::class.java
        )
        if (rsp == null) {
            return false
        }
        _economy = rsp.provider
        enabled = true
        plugin.consoleMessage("<gold>Vault</gold> dependency loaded.")
        return true
    }
}
