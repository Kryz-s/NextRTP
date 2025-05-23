package ur.kryz.rtp.modules.main

import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.animation.ParticleManager
import ur.kryz.rtp.command.paper.NextRootCommand
import ur.kryz.rtp.file.YamlFile
import ur.kryz.rtp.manager.CooldownManager
import ur.kryz.rtp.modules.PluginModule
import ur.kryz.rtp.modules.main.command.RtpCommand
import ur.kryz.rtp.modules.main.command.RtpSudoCommand
import ur.kryz.rtp.modules.main.command.RtpWorldCommand
import ur.kryz.rtp.modules.main.task.RtpTask
import ur.kryz.rtp.modules.main.task.TeleportScheduler

class RandomTeleportModule(plugin: NextRTPPlugin) : PluginModule(plugin) {

    private lateinit var yaml: YamlFile
    private lateinit var _task: RtpTask
    private lateinit var _particle: ParticleManager
    private var _cooldown: CooldownManager? = null

    override fun onEnable() {
        yaml = loadConfig("rtp.yml")

        NextRootCommand.add(RtpCommand(this))
        NextRootCommand.add(RtpSudoCommand(this))
        NextRootCommand.add(RtpWorldCommand(this))

        RandomTeleportProcessor.set(yaml)

        _task = RtpTask(this)
        _task.runTaskTimerAsynchronously(plugin, 0L, 5L)
        _cooldown = CooldownManager(yaml.get("teleport.cooldown", 30))

        _particle = ParticleManager(plugin)
    }

    override fun onDisable() {
        if(isEnabled()) {
            _task.cancel()
            if(_particle.isRunning()) {
                _particle.cancel()
            }
        }
    }

    override fun reload() {
        yaml.load()
        _task.set(TeleportScheduler(this))
        _task.loadSounds()
        _cooldown = CooldownManager(yaml.get("teleport.cooldown", 30))
        RandomTeleportProcessor.reload()
    }

    override fun id(): String {
        return "rtp"
    }

    val config: YamlFile
        get() = yaml
    val task: RtpTask
        get() = _task
    val particleAnimationManager: ParticleManager
        get() = _particle
    val cooldownManager: CooldownManager
        get() = _cooldown ?: error("CooldownManager is null.")
}