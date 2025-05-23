package ur.kryz.rtp.modules

import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.file.YamlFile

abstract class PluginModule(open val plugin: NextRTPPlugin) {

    private var isEnabled: Boolean = false

    val moduleName: String
        get() = this.javaClass.simpleName

    abstract fun onEnable()

    abstract fun onDisable()

    abstract fun id() : String

    open fun loadCommands() {}

    open fun reload() {}

    fun setEnabled(boolean: Boolean) {
        if(boolean) {
            try {
                onEnable()
                this.isEnabled = true
                plugin.consoleMessage("<gold>$moduleName</gold> loaded.")
            } catch (e: Exception) {
                onDisable()
                this.isEnabled = false
                throw RuntimeException("Failed to enable module $moduleName", e)
            }
        }
        else if (isEnabled()) {
            onDisable()
            this.isEnabled = false
        }
    }

    fun isEnabled(): Boolean {
        return this.isEnabled
    }

    fun loadConfig(name: String) : YamlFile {
        return YamlFile.load(name, plugin)
    }
}
