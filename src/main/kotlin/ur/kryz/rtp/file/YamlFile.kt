package ur.kryz.rtp.file

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor
import org.yaml.snakeyaml.representer.Representer
import ur.kryz.rtp.NextRTPPlugin
import java.io.*
import java.nio.file.Files
import javax.annotation.Nonnull

class YamlFile(private val file: File, private val stream: InputStream) {
    private val yaml: Yaml
    private var rawData: MutableMap<String, Any>? = null
    private val flatCache: MutableMap<String, Any> = HashMap()
    private val sectionCache: MutableMap<String, YamlSection> = HashMap()

    init {
        val options = DumperOptions()
        options.indent = 2
        options.isPrettyFlow = true
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

        val representer = Representer(options)
        representer.propertyUtils.isSkipMissingProperties = true

        val loaderOptions = LoaderOptions()
        val constructor = SafeConstructor(loaderOptions)

        this.yaml = Yaml(constructor, representer, options)
        load()
    }

    private fun createParentDirs() {
        val path = file.toPath()
        try {
            val parent = path.parent
            if (parent != null) {
                Files.createDirectories(parent)
                check(Files.isDirectory(parent)) { "Unable to create parent directories for $path" }
            }
        } catch (e: IOException) {
            throw IllegalStateException("Failed to create parent directories for $path", e)
        }
    }

    fun load() {
        if (!file.exists()) {
            val path = file.toPath()
            rawData = LinkedHashMap()
            createParentDirs()
            try {
                stream.use { input ->
                    Files.copy(input, path)
                }
            } catch (e: IOException) {
                throw IllegalStateException("Failed to copy default configuration from resource to $path", e)
            }
        }

        try {
            FileInputStream(file).use { input ->
                val loaded = yaml.load<Any>(input)
                rawData = if (loaded is Map<*, *>) {
                    loaded as MutableMap<String, Any>
                } else {
                    LinkedHashMap()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            rawData = LinkedHashMap()
        }

        flatCache.clear()
        sectionCache.clear()
        flattenMap("", rawData, flatCache)
    }

    fun save() {
        try {
            FileWriter(file).use { writer ->
                yaml.dump(rawData, writer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun flattenMap(prefix: String, source: Map<String, Any>?, target: MutableMap<String, Any>) {
        for ((key1, value) in source!!) {
            val key = if (prefix.isEmpty()) key1 else "$prefix.$key1"
            if (value is Map<*, *>) {
                flattenMap(key, value as Map<String, Any>, target)
            } else {
                target[key] = value
            }
        }
    }

    fun <T> get(path: String): T? {
        return flatCache[path] as T?
    }

    @Nonnull
    fun <T> get(path: String, def: T): T {
        val value = flatCache[path] as T?
        return value ?: def
    }

    fun contains(path: String): Boolean {
        return flatCache.containsKey(path)
    }

    fun set(path: String, value: Any) {
        setInNestedMap(rawData, path, value)
        flatCache[path] = value
        sectionCache.clear()
    }

    private fun setInNestedMap(map: MutableMap<String, Any>?, path: String, value: Any) {
        val keys = path.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var current = map
        for (i in 0 until keys.size - 1) {
            current =
                current!!.computeIfAbsent(keys[i]) { k: String? -> LinkedHashMap<String, Any>() } as MutableMap<String, Any>
        }
        current!![keys[keys.size - 1]] = value
    }

    fun getSection(path: String): YamlSection? {
        if (sectionCache.containsKey(path)) return sectionCache[path]

        val section = getNestedMap(rawData, path) ?: return null

        val yamlSection = YamlSection(path, section)
        sectionCache[path] = yamlSection
        return yamlSection
    }

    private fun getNestedMap(map: Map<String, Any>?, path: String): Map<String, Any>? {
        val keys = path.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var current: Any? = map
        for (key in keys) {
            if (current is Map<*, *>) {
                current = current[key]
            } else {
                return null
            }
        }
        return if ((current is Map<*, *>)) current as Map<String, Any>? else null
    }

    val keys: Set<String>
        get() = flatCache.keys

    val all: Map<String, Any>
        get() = flatCache

    companion object {
        @JvmStatic
        fun load(name: String, plugin: NextRTPPlugin): YamlFile {
            val file = File(plugin.dataFolder, name)
            val stream = plugin.getResource(name) ?: throw IllegalStateException("Resource not found: $name")
            return YamlFile(file, stream)
        }
    }
}
