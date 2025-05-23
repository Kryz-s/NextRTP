package ur.kryz.rtp.modules

import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.modules.main.RandomTeleportModule
import ur.kryz.rtp.modules.sign.SignModule

class ModuleManager(private val plugin: NextRTPPlugin) {
    private val moduleMap: MutableMap<Class<*>, PluginModule> = HashMap()

    fun loadModules() {
        val ms = System.currentTimeMillis()

        this.register(RandomTeleportModule(plugin))
        this.register(SignModule(plugin))

        val total = System.currentTimeMillis() - ms
        plugin.consoleMessage("<gold>${moduleMap.size}</gold> modules loaded in <gold>${total}ms</gold>")
    }

    fun disableModules() {
        val keys = moduleMap.keys.toList()
        for (s in keys) {
            this.disableModule(s)
        }
    }

    fun <T : PluginModule> getModule(clazz: Class<T>) : T {
        @Suppress("UNCHECKED_CAST")
        return (moduleMap[clazz]!! as T)
    }

    fun getModules() : Set<PluginModule> {
        val hash: MutableSet<PluginModule> = HashSet()
        val entries = moduleMap.entries
        for(entry in entries){
            hash.add(entry.value)
        }
        return hash
    }

    private fun register(pluginModule: PluginModule) {
        if(pluginModule is RandomTeleportModule) {
            moduleMap[pluginModule.javaClass] = pluginModule
            pluginModule.setEnabled(true)
            return
        }
        if(plugin.config.getBoolean("modules.${pluginModule.id()}", true)) {
            moduleMap[pluginModule.javaClass] = pluginModule
            pluginModule.setEnabled(true)
        }
    }

    fun <T : PluginModule> isModuleEnabled(clazz: Class<T>) : Boolean {
        return this.moduleMap[clazz]!!.isEnabled()
    }

    private fun disableModule(clazz: Class<*>) {
        val module = moduleMap.remove(clazz)!!
        if(module.isEnabled()) {
            plugin.consoleMessage("Disabling <gold>${module.moduleName}</gold>")
            module.setEnabled(false)
        }
    }
}
