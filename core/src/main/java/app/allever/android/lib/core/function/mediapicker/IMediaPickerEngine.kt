package app.allever.android.lib.core.function.mediapicker

import android.content.Context

interface IMediaPickerEngine {
    suspend fun launchPicker(context: Context, vararg type: String): MediaPickerResult
}