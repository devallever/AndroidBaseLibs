package app.allever.android.lib.core.function.player.kernel.internal

/***
 * 播放器常用功能
 */
interface IFunctionPlayer {

    /*----------------------------第二部分：视频播放器状态方法----------------------------------*/
    /**
     * 播放
     */
    fun start()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 停止
     */
    fun stop()

    /**
     * 重置播放器
     */
    fun reset()

    /**
     * 是否正在播放
     * @return 是否正在播放
     */
    fun isPlaying(): Boolean

    /**
     * 调整进度
     */
    fun seekTo(time: Long)

    /**
     * 释放播放器
     */
    fun release()

    /**
     * 获取当前播放的位置
     * @return 获取当前播放的位置
     */
    fun getCurrentPosition(): Long

    /**
     * 获取视频总时长
     * @return 获取视频总时长
     */
    fun getDuration(): Long

    /**
     * 获取缓冲百分比
     * @return 获取缓冲百分比
     */
    fun getBufferedPercentage(): Int

    /**
     * 设置音量
     * @param v1                                v1
     * @param v2                                v2
     */
    fun setVolume(v1: Float, v2: Float)

    /**
     * 设置是否循环播放
     * @param isLooping 是否循环播放
     */
    fun setLooping(isLooping: Boolean)

    /**
     * 设置其他播放配置
     */
    fun setOptions()

    /**
     * 设置播放速度
     * @param speed 速度
     */
    fun setSpeed(speed: Float)

    /**
     * 获取播放速度
     * @return 播放速度
     */
    fun getSpeed(): Float

    /**
     * 获取当前缓冲的网速
     * @return 获取网络
     */
    fun getTcpSpeed(): Long

}