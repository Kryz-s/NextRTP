package ur.kryz.rtp.adapter

import net.kyori.adventure.key.Key
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class BiomeAdapter {
    private var getKey: Method? = null

    init {
        try {
            val biomeClass = Class.forName("org.bukkit.block.Biome")
            getKey = biomeClass.getMethod("getKey")
        } catch (_: ClassNotFoundException) {
//            throw RuntimeException(e)
        } catch (_: NoSuchMethodException) {
//            throw RuntimeException(e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Key> getKey(any: Any): T? {
        try {
            return getKey!!.invoke(any) as T
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: InvocationTargetException) {
        }
        return null
    }

    companion object {
        val instance: BiomeAdapter by lazy { BiomeAdapter() }
    }
}
