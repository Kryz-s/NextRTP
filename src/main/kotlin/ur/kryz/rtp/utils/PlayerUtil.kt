package ur.kryz.rtp.utils

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.file.YamlFile
import ur.kryz.rtp.hook.PlaceholderAPIHook
import ur.kryz.rtp.hook.VaultHook.Companion.getInstance
import ur.kryz.rtp.parser.LegacyParser

object PlayerUtil {
    private var yaml: YamlFile? = null
    private val vault = getInstance()

    fun set(yamlFile: YamlFile) {
        yaml = yamlFile
    }

    @JvmStatic
    fun takeMoney(player: Player, amount: Double) {
        if (vault.isEnabled()) {
            vault.economy.withdrawPlayer(player, amount)
            sendParsed(
                player, "messages.currency.charged",
                Placeholder.unparsed("amount", amount.toString())
            )
        }
    }

    @JvmStatic
    fun hasMoney(player: Player?, amount: Double): Boolean {
        if (vault.isEnabled()) {
            val balance = vault.economy.getBalance(player)
            return balance >= amount
        } else {
            return true
        }
    }

    @JvmStatic
    fun sendParsed(sender: CommandSender, key: String, vararg singles: TagResolver.Single) {
        val prefix =
            yaml!!.get("messages.prefix", "<gray><bold>⤷</bold> <gradient:blue:aqua>UrRTP</gradient> <gray>»</gray>")
        val msg = yaml!!.get(key, "<red>Message not found: $key</red>")

        var parsedPrefix = LegacyParser.parse(prefix)
        var parsedMsg = LegacyParser.parse(msg)
        if (sender is Player) {
            val inst = PlaceholderAPIHook.getInstance()
            if (inst != null) {
                parsedPrefix = inst.setPlaceholders(sender, parsedPrefix)
                parsedMsg = inst.setPlaceholders(sender, parsedMsg)
            }
        }
        val prefixResolver = Placeholder.parsed("prefix", parsedPrefix)

        sender.sendMessage(
            NextRTPPlugin.MINI_MESSAGE.deserialize(
                parsedMsg,
                prefixResolver,
                *singles
            )
        )
    }
}
