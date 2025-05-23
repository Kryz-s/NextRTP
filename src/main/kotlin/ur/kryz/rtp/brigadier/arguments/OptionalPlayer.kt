package ur.kryz.rtp.brigadier.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.CompletableFuture

class OptionalPlayer : CustomArgumentType<Optional<Player>, PlayerSelectorArgumentResolver> {

    companion object {
        @JvmStatic
        fun player() : OptionalPlayer {
            return OptionalPlayer()
        }
    }

    override fun parse(reader: StringReader): Optional<Player> {
        val input = reader.readUnquotedString()

        return Optional.ofNullable(Bukkit.getPlayer(input))
    }

    override fun getNativeType(): ArgumentType<PlayerSelectorArgumentResolver> {
        return ArgumentTypes.player()
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {

        Bukkit.getOnlinePlayers()
            .map{ t -> t.name}
            .forEach{ t -> builder.suggest(t) }

        return builder.buildFuture()
    }
}
