package app.allever.android.lib.core.function.imagecompress

import android.content.Context

interface IIMageCompress {
    suspend fun compress(context: Context, path: String): CompressResult

    suspend fun compress(context: Context, pathList: MutableList<String>): CompressResult
}