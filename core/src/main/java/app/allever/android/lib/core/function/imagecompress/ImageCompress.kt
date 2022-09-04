package app.allever.android.lib.core.function.imagecompress

import android.content.Context

object ImageCompress {

    private var mEngine: IIMageCompress? = null

    fun init(engine: IIMageCompress) {
        mEngine = engine
    }

    suspend fun compress(context: Context, path: String): CompressResult {
        return mEngine?.compress(context, path)!!
    }

    suspend fun compress(context: Context, pathList: MutableList<String>): CompressResult {
        return mEngine?.compress(context, pathList)!!
    }
}