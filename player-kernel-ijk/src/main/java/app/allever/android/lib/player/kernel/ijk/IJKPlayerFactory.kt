package app.allever.android.lib.player.kernel.ijk

import app.allever.android.lib.core.function.player.kernel.internal.AbsPlayerFactory

/**
 * IJKPlayer工厂类
 */
class IJKPlayerFactory : AbsPlayerFactory() {
    override fun createPlayer() = IJKPlayer()
}