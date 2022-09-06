package app.allever.android.lib.core.function.mediapicker

import android.content.Context
import app.allever.android.lib.core.function.media.MediaHelper

object MediaPickerHelper : IMediaPickerEngine {

    const val TYPE_IMAGE = MediaHelper.TYPE_IMAGE
    const val TYPE_VIDEO = MediaHelper.TYPE_VIDEO
    const val TYPE_AUDIO = MediaHelper.TYPE_AUDIO
    const val TYPE_ALL = MediaHelper.TYPE_ALL

    private var mEngine: IMediaPickerEngine? = null

    fun init(engine: IMediaPickerEngine?) {
        mEngine = engine
    }

    override suspend fun launchPicker(context: Context, vararg type: String): MediaPickerResult {
        return mEngine?.launchPicker(context, *type)!!
    }
}