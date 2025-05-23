package ur.kryz.rtp.command.paper

import com.mojang.brigadier.builder.LiteralArgumentBuilder

@Suppress("UnstableApiUsage")
interface SubCommand<S> : ExecutorContext<S> {

    fun build(builder: LiteralArgumentBuilder<S>) : LiteralArgumentBuilder<S>
}