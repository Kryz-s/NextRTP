package ur.kryz.rtp.utils

import java.util.concurrent.TimeUnit

object DateFormatUtil {
    @JvmStatic
    fun formatTime(seconds: Int): String {
        return if (seconds < 60) {
            seconds.toString() + "s"
        } else if (seconds < 3600) {
            (seconds / 60).toString() + "m " + (seconds % 60) + "s"
        } else if (seconds < 86400) {
            (seconds / 3600).toString() + "h " + ((seconds % 3600) / 60) + "m"
        } else {
            (seconds / 86400).toString() + "d " + ((seconds % 86400) / 3600) + "h"
        }
    }

//    fun formatTimeDetailed(seconds: Long): String {
//        val days = TimeUnit.SECONDS.toDays(seconds)
//        val hours = TimeUnit.SECONDS.toHours(seconds) % 24
//        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
//        val secs = seconds % 60
//
//        val sb = StringBuilder()
//        if (days > 0) sb.append(days).append("d ")
//        if (hours > 0) sb.append(hours).append("h ")
//        if (minutes > 0) sb.append(minutes).append("m ")
//        if (secs > 0 || sb.length == 0) sb.append(secs).append("s")
//
//        return sb.toString().trim { it <= ' ' }
//    }
}
