package ur.kryz.rtp.utils

import org.bukkit.Bukkit
import kotlin.math.max

object VersionUtil {
    private val version = Bukkit.getMinecraftVersion()
    private val splitter = version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    fun isGreaterThan(v2: String): Boolean {
        return compareVersions(v2) > 0
    }

    fun isLessThanOrEqualTo(v2: String): Boolean {
        return compareVersions(v2) <= 0
    }

    private fun compareVersions(v2: String): Int {
        val parts2 = v2.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val length = max(splitter.size.toDouble(), parts2.size.toDouble())
            .toInt()

        for (i in 0 until length) {
            val num1 = if (i < splitter.size) splitter[i].toInt() else 0
            val num2 = if (i < parts2.size) parts2[i].toInt() else 0

            if (num1 != num2) {
                return Integer.compare(num1, num2)
            }
        }
        return 0
    }
}
