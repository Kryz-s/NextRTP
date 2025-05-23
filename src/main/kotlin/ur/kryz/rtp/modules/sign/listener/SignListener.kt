package ur.kryz.rtp.modules.sign.listener

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import ur.kryz.rtp.api.events.SignTeleportStartEvent
import ur.kryz.rtp.modules.main.RandomTeleportModule
import ur.kryz.rtp.modules.main.command.RtpCommand.Util.rtp
import ur.kryz.rtp.modules.sign.SignModule
import ur.kryz.rtp.utils.PlayerUtil.sendParsed

class SignListener(private val module: SignModule) : Listener {
    private val namespacedKey = NamespacedKey(module.plugin, "rtp_sign")
    private val plain = PlainTextComponentSerializer.plainText()

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onSign(event: SignChangeEvent) {
        val player = event.player
        val lines = event.lines()
        val config = module.signFile

        if (!player.hasPermission("urrtp.sign")) return
        if (!isRtpSign(lines, config!!.get<String>("sign.line"), config.get("sign.ignore-case", true))) return
        val block = event.block
        val sign = block.state as Sign
        val l = block.location

        val container = sign.persistentDataContainer
        val `is` = container.get(namespacedKey, PersistentDataType.BOOLEAN)
        if (container.has(namespacedKey)) return
        if (`is` == false || `is` != null) return

        container.set(namespacedKey, PersistentDataType.BOOLEAN, true)
        if (!sign.update()) throw UnsupportedOperationException("The update of the sign not success.")
        sendParsed(
            player, "messages.sign.success",
            Placeholder.unparsed("x", l.x.toString()),
            Placeholder.unparsed("y", l.y.toString()),
            Placeholder.unparsed("z", l.z.toString())
        )
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        //        System.out.println("No es null");
        val state = block.state as? Sign ?: return

        //        System.out.println("Es Sign");
        val container = state.persistentDataContainer
        val `is` = container.get(namespacedKey, PersistentDataType.BOOLEAN) ?: return

        //        System.out.println("No es null");
        if (!`is`) return

        //        System.out.println("No es false");
        val player = event.player
        val mE = SignTeleportStartEvent(player, state)

        Bukkit.getPluginManager().callEvent(mE)

        if (mE.isCancelled) return

        rtp(module.plugin.getModuleManager().getModule(RandomTeleportModule::class.java), player, player.world)
    }

    private fun isRtpSign(lines: List<Component>, expectedLine: String?, ignoreCase: Boolean): Boolean {
        for (line in lines) {
            val text = plain.serialize(line)
            if (ignoreCase) {
                if (text.equals(expectedLine, ignoreCase = true)) {
                    return true
                }
            } else {
                if (text == expectedLine) {
                    return true
                }
            }
        }
        return false
    }
}
