package app.allever.android.lib.widget.mediapicker

import androidx.fragment.app.FragmentManager
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.widget.mediapicker.ui.MediaPickerActivity
import app.allever.android.lib.widget.mediapicker.ui.MediaPickerDialog

object MediaPicker {
    fun launchPickerActivity(vararg type: String) {
        ActivityHelper.startActivity<MediaPickerActivity> {
            putStringArrayListExtra(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST, vararg2List(*type))
        }
    }

    fun launchPickerDialog(fragmentManager: FragmentManager, vararg type: String) {
        MediaPickerDialog(vararg2List(*type)).show(fragmentManager)
    }

    private fun vararg2List(vararg data: String): ArrayList<String> {
        val list = ArrayList<String>()
        data.map {
            list.add(it)
        }
        return list
    }
}