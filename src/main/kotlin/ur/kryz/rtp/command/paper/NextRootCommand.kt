package ur.kryz.rtp.command.paper

import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent

object NextRootCommand {

    private val commands: MutableSet<SubCommand<CommandSourceStack>> = HashSet()

    private val aliases: List<String> = listOf(
        "rtp",
        "tpr"
    )

    @JvmStatic
    fun registrar(commands: ReloadableRegistrarEvent<Commands>) {
        val root = Commands.literal("nextrtp")

        for (sub in this.commands) {
            sub.build(root)
        }

        val node = root.build()

        commands.registrar().register(node,"NextRTP main command", aliases)
    }

    @JvmStatic
    fun add(sub: SubCommand<CommandSourceStack>) {
        this.commands.add(sub)
    }
}
