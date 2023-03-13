package app.allever.android.lib.core.function.player.kernel

import app.allever.android.lib.core.function.player.kernel.internal.AbsPlayerFactory

/**
 * MediaPlayer工厂类
 */
class AndroidPlayerFactory : AbsPlayerFactory() {
    override fun createPlayer() = AndroidPlayer()
}