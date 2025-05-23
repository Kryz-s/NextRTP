package ur.kryz.rtp.test

import java.util.Optional

object TestClass {

    @JvmStatic
    fun main(args: Array<String>) {
        val value = 1000L
        val ticks = 5

        val clazz = Optional::class.java as Class<Optional<String>>

        val playerClass : Optional<String> = Optional.empty()

        val clazsd = playerClass.javaClass

        println(playerClass.javaClass)
    }
}
