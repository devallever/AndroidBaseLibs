package app.allever.android.lib.widget.mediapicker.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.helper.FragmentHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.ActivityMediaPickerBinding
import app.allever.android.lib.widget.mediapicker.MediaPicker
import kotlinx.coroutines.launch

class MediaPickerActivity : AbstractActivity(), MediaPickerFragment.Callback {
    companion object {
        const val EXTRA_MEDIA_TYPE_LIST = MediaPickerFragment.EXTRA_MEDIA_TYPE_LIST
    }

    private val mViewModel by viewModels<MediaPickerViewModel>()

    private lateinit var mBinding: ActivityMediaPickerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_media_picker)
        mViewModel.initExtra(intent)
        val fragment = FragmentHelper.newInstance(MediaPickerFragment::class.java) {
            putStringArrayList(EXTRA_MEDIA_TYPE_LIST, mViewModel.typeList)
        }
        fragment.setCallback(this)
        FragmentHelper.addToContainer(
            supportFragmentManager,
            fragment,
            mBinding.fragmentContainer.id
        )
    }

    override fun finish() {
        super.finish()
        CoroutineHelper.DEFAULT.launch {
            MediaPicker.fetchFromPhone(App.context, MediaHelper.TYPE_IMAGE)
            MediaPicker.fetchFromPhone(App.context, MediaHelper.TYPE_VIDEO)
            MediaPicker.fetchFromPhone(App.context, MediaHelper.TYPE_AUDIO)
            MediaPicker.fetchFolderList(App.context, mViewModel.typeList)
        }
    }

    override fun onFinish() {
        finish()
        MediaPicker.listeners().clear()
    }
}

class MediaPickerViewModel : ViewModel() {
    lateinit var typeList: ArrayList<String>
    fun initExtra(intent: Intent?) {
        typeList =
            intent?.getStringArrayListExtra(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST) as? ArrayList<String>
                ?: ArrayList()
        typeList.map {
            log("MediaPickerActivity 媒体类型：${it}")
        }
    }
}