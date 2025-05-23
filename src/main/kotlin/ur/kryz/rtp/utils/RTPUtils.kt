package ur.kryz.rtp.utils

import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.manager.CooldownManager
import ur.kryz.rtp.modules.main.task.TeleportScheduler
import ur.kryz.rtp.utils.PlayerUtil.sendParsed
import ur.kryz.rtp.utils.PlayerUtil.takeMoney

object RTPUtils {
    @JvmStatic
    fun processSuccess(cooldownManager: CooldownManager, scheduler: TeleportScheduler, player: Player, money: Number) {
        if (!player.hasPermission("nextrtp.bypass.cooldown")) {
            cooldownManager.applyCooldown(player)
        }
        if (scheduler.isShowTitles && scheduler.isShowSuccessTitles) {
            player.showTitle(
                Title.title(
                    NextRTPPlugin.MINI_MESSAGE.deserialize(scheduler.title),
                    NextRTPPlugin.MINI_MESSAGE.deserialize(scheduler.subtitle)
                )
            )
            player.sendActionBar(NextRTPPlugin.MINI_MESSAGE.deserialize(scheduler.actionbar))
        }
        if (scheduler.isSendSound && scheduler.isSendSuccessSound) {
            player.playSound(scheduler.successSound!!)
        }
        if (scheduler.isShowSuccessMessage) {
            sendParsed(player, "messages.teleport.success")
        }
        if (!player.hasPermission("nextrtp.bypass.money") && money.toDouble() > 0) {
            takeMoney(player, money.toDouble())
        }
    }
}
