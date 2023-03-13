package app.allever.android.lib.core.function.player.kernel.internal

import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder

/**
 * 初始化相关功能
 */
interface IInitPlayer {
    /**
     * 第一步初始化播放器
     */
    fun initPlayer()

    /**
     * 第二步：设置数据源
     */
    /**
     * @param path url或本地路径
     */
    fun setDataSource(path: String, headers: Map<String, String>? = null)

    /**
     * 用于播放raw和asset里面的视频文件
     */
    fun setDataSource(accessFileDescriptor: AssetFileDescriptor)

    /**
     * @param path url或本地路径
     */
    fun setDataSource(uri: Uri, headers: Map<String, String>? = null)

    /**
     * 设置渲染视频的View,主要用于TextureView
     * 第三步：设置surface
     * @param surface                           surface
     */
    fun setSurface(surface: Surface?)

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     * @param holder holder
     */
    fun setDisplay(holder: SurfaceHolder?)

    /**
     * 第四步：准备播放
     */
    fun prepareAsync()
}