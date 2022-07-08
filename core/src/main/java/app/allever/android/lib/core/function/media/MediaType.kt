package app.allever.android.lib.core.function.media

import androidx.annotation.IntDef

object MediaType {

    const val IMAGE = -1

    const val VIDEO = -2

    /**
     * png jpg
     */
    const val TYPE_OTHER_IMAGE = 0

    /**
     * GIF
     */
    const val TYPE_GIF = 1

    /**
     * png
     */
    const val TYPE_PNG = 2

    /**
     * jpg
     */
    const val TYPE_JPG = 3

    /**
     * video
     */
    const val TYPE_VIDEO = 4

    /**
     * text
     */
    const val TYPE_TEXT = 5

    /**
     * audio
     */
    const val TYPE_AUDIO = 6

    /**
     * sticker
     */
    const val TYPE_STICKER = 7

    @IntDef(value = [TYPE_OTHER_IMAGE, TYPE_JPG, TYPE_PNG, TYPE_GIF, TYPE_VIDEO, TYPE_TEXT, TYPE_AUDIO, TYPE_STICKER])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Type

    fun isGif(type: Int): Boolean {
        return type == TYPE_GIF
    }

    fun isPNG(type: Int): Boolean {
        return type == TYPE_PNG
    }

    fun isJPG(type: Int): Boolean {
        return type == TYPE_JPG
    }

    fun isJPGOrPNG(type: Int): Boolean {
        return type == TYPE_JPG || type == TYPE_PNG
    }

    fun isOtherImage(type: Int): Boolean {
        return type == TYPE_OTHER_IMAGE
    }

    fun isImage(type: Int): Boolean {
        return type == TYPE_OTHER_IMAGE || type == TYPE_GIF || type == TYPE_PNG || type == TYPE_JPG
    }

    fun isVideo(type: Int): Boolean {
        return type == TYPE_VIDEO
    }

    fun isText(type: Int): Boolean {
        return type == TYPE_TEXT
    }

    fun isAudio(type: Int): Boolean {
        return type == TYPE_AUDIO
    }

    fun isSticker(type: Int): Boolean {
        return type == TYPE_STICKER
    }
}