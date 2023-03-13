package app.allever.android.lib.core.function.player.kernel.internal

import app.allever.android.lib.core.ext.log

/***
 * 抽象播放器类
 */
abstract class AbsPlayer : IInitPlayer, IFunctionPlayer {
    var playerStatusListener: PlayerStatusListener? = null
        set(value) {
            log("设置播放器状态监听器")
            field = value
        }

}