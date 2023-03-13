package app.allever.android.lib.core.function.player.kernel.internal

/***
 * 抽象工厂类
 */
abstract class AbsPlayerFactory {
    companion object {
        inline fun <reified F> create(): F = F::class.java.newInstance()
    }

    abstract fun createPlayer(): AbsPlayer
}