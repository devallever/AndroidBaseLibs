package app.allever.android.lib.widget.mediapicker

import androidx.fragment.app.FragmentManager
import app.allever.android.lib.core.function.media.FolderBean
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.widget.mediapicker.ui.MediaPickerActivity
import app.allever.android.lib.widget.mediapicker.ui.MediaPickerDialog

object MediaPicker {

    private val mMediaPickerListenerSet = mutableListOf<MediaPickerListener>()

    val cacheFolderList = mutableListOf<FolderBean>()
    val cacheAllImageBeanList = mutableListOf<MediaBean>()
    val cacheAllVideoBeanList = mutableListOf<MediaBean>()
    val cacheAllAudioBeanList = mutableListOf<MediaBean>()

    fun launchPickerActivity(
        vararg type: String,
        mediaPickerListener: MediaPickerListener? = null
    ) {
        mediaPickerListener?.let {
            mMediaPickerListenerSet.add(mediaPickerListener)
        }
        ActivityHelper.startActivity<MediaPickerActivity> {
            putStringArrayListExtra(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST, vararg2List(*type))
        }
    }

    fun launchPickerDialog(
        fragmentManager: FragmentManager,
        vararg type: String,
        mediaPickerListener: MediaPickerListener? = null
    ) {
        mediaPickerListener?.let {
            mMediaPickerListenerSet.add(mediaPickerListener)
        }
        MediaPickerDialog(vararg2List(*type)).show(fragmentManager)
    }

    fun listeners() = mMediaPickerListenerSet

    private fun vararg2List(vararg data: String): ArrayList<String> {
        val list = ArrayList<String>()
        data.map {
            list.add(it)
        }
        return list
    }
}