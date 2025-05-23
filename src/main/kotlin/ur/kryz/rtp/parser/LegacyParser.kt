package ur.kryz.rtp.parser

import java.util.regex.MatchResult
import java.util.regex.Pattern

object LegacyParser {
    private val REPLACES: MutableMap<Char, String> = HashMap()
    private val HEX_PATTERN: Pattern = Pattern.compile("&#([A-Fa-f0-9]{6})")

    init {
        REPLACES['0'] = "<black>"
        REPLACES['1'] = "<dark_blue>"
        REPLACES['2'] = "<dark_green>"
        REPLACES['3'] = "<dark_aqua>"
        REPLACES['4'] = "<dark_red>"
        REPLACES['5'] = "<dark_purple>"
        REPLACES['6'] = "<gold>"
        REPLACES['7'] = "<gray>"
        REPLACES['8'] = "<dark_gray>"
        REPLACES['9'] = "<blue>"
        REPLACES['a'] = "<green>"
        REPLACES['b'] = "<aqua>"
        REPLACES['c'] = "<red>"
        REPLACES['d'] = "<light_purple>"
        REPLACES['e'] = "<yellow>"
        REPLACES['f'] = "<white>"
        REPLACES['k'] = "<magic>"
        REPLACES['l'] = "<bold>"
        REPLACES['m'] = "<strikethrough>"
        REPLACES['n'] = "<underline>"
        REPLACES['o'] = "<italic>"
        REPLACES['r'] = "<reset>"
    }

    /**
     * Converts legacy message to MiniMessage
     *
     * @param legacy Legacy message
     * @return MiniMessage-compatible format
     */
    @JvmStatic
    fun parse(legacy: String): String {
        var builder = ""
        val chars = legacy.toCharArray()

        var i = 0
        while (i < chars.size) {
            if (chars[i] == '&' && i + 1 < chars.size) {
                val next = chars[i + 1]
                val replacement = REPLACES[next]
                if (replacement != null) {
                    builder += replacement
                    i += 2
                    continue
                }
            }
            builder += chars[i]
            i++
        }

        var result = builder
        val matcher = HEX_PATTERN.matcher(result)
        result = matcher.replaceAll { mr: MatchResult -> "<#" + mr.group(1) + ">" }

        return result
    }
}
