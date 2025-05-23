package ur.kryz.rtp.modules.location

import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.file.YamlFile
import ur.kryz.rtp.file.YamlFile.Companion.load
import ur.kryz.rtp.modules.PluginModule

class LocationModule(plugin: NextRTPPlugin) : PluginModule(plugin) {
     private var locationConfig: YamlFile? = null
         get() = field!!

    override fun onEnable() {
        locationConfig = load("locations.yml", plugin)
    }

    override fun onDisable() {
    }

    override fun id(): String {
        return "location"
    }

    override fun reload() {
        locationConfig!!.load()
    }
}
