package ur.kryz.rtp.brigadier.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import org.bukkit.Bukkit
import org.bukkit.World
import java.util.*
import java.util.concurrent.CompletableFuture

class BukkitWorldArgument : CustomArgumentType<Optional<World>, String> {
    override fun parse(p0: StringReader): Optional<World> {
        val read = p0.readString()
        val world = Bukkit.getWorld(read)
        return Optional.ofNullable(world)
    }

    override fun getNativeType(): ArgumentType<String> {
        return StringArgumentType.word()
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {

        Bukkit.getWorlds()
            .map(World::getName)
            .map(builder::suggest)

        return builder.buildFuture()
    }
}
