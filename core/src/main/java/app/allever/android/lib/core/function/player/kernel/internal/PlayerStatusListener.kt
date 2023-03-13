package app.allever.android.lib.core.function.player.kernel.internal

/**
 * 播放器状态监听器
 */
interface PlayerStatusListener {

    /**
     * 异常
     * 1          表示错误的链接
     * 2          表示解析异常
     * 3          表示其他的异常
     * @param type                          错误类型
     */
    fun onError(@PlayerConstant.ErrorType type: Int, error: String?)

    /**
     * 完成
     */
    fun onCompletion()

    /**
     * 视频信息
     * @param what                          what
     * @param extra                         extra
     */
    fun onInfo(what: Int, extra: Int)

    /**
     * 准备
     */
    fun onPrepared()

    /**
     * 视频size变化监听
     * @param width                         宽
     * @param height                        高
     */
    fun onVideoSizeChanged(width: Int, height: Int)

}