package ur.kryz.rtp.modules.sign

import org.bukkit.Bukkit
import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.file.YamlFile
import ur.kryz.rtp.file.YamlFile.Companion.load
import ur.kryz.rtp.modules.PluginModule
import ur.kryz.rtp.modules.sign.listener.SignListener

class SignModule(plugin: NextRTPPlugin?) : PluginModule(plugin!!) {
    val signFile: YamlFile = load("sign.yml", plugin!!)

    override fun onEnable() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(SignListener(this), plugin)
    }

    override fun onDisable() {
        // Nothing to disable
    }

    override fun id(): String {
        return "sign"
    }
}
