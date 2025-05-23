package ur.kryz.rtp.modules.main.task

import org.bukkit.Location
import org.bukkit.World

data class RtpContainer(
    val time: Int,
    val loc: Location,
    val world: World,
    var teleporting: Boolean = false,
    var tickCounter: Int = 0
) {
    var seconds : Int = 0

    fun isEquals(act: Location) : Boolean {
        return loc.x.toInt() == act.x.toInt() && loc.y.toInt() == act.y.toInt() && loc.z.toInt() == act.z.toInt()
    }
}
