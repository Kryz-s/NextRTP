package ur.kryz.rtp.file

import org.intellij.lang.annotations.Subst

class YamlSection(val path: String, private val map: Map<String, Any>) {
    private val sectionCache: MutableMap<String, YamlSection> = HashMap()

    val keys: Set<String>
        get() = map.keys

    @Subst("")
    fun <T> get(key: String): T? {
        return map[key] as T?
    }

    fun <T> get(key: String, def: T): T {
        val value = map[key] as T?
        return value ?: def
    }

    fun getSection(key: String): YamlSection? {
        if (sectionCache.containsKey(key)) return sectionCache[key]

        val value = map[key]
        if (value is Map<*, *>) {
            val section = YamlSection("$path.$key", value as Map<String, Any>)
            sectionCache[key] = section
            return section
        }
        return null
    }

    fun contains(key: String): Boolean {
        return map.containsKey(key)
    }
}
