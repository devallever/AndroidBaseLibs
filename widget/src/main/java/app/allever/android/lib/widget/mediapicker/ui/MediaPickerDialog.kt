package app.allever.android.lib.widget.mediapicker.ui

import app.allever.android.lib.core.base.dialog.AbstractSlideBottomDialog
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.helper.FragmentHelper
import app.allever.android.lib.widget.databinding.DialogMediaPickerBinding
import app.allever.android.lib.widget.mediapicker.MediaPicker

class MediaPickerDialog(val typeList: ArrayList<String>) :
    AbstractSlideBottomDialog<DialogMediaPickerBinding>(), MediaPickerFragment.Callback {

    override fun inflateBinding() = DialogMediaPickerBinding.inflate(layoutInflater)

    override fun initView() {
        val fragment = FragmentHelper.newInstance(MediaPickerFragment::class.java) {
            putStringArrayList(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST, typeList)
        }
        fragment.setCallback(this)
        FragmentHelper.addToContainer(
            childFragmentManager,
            fragment,
            mBinding.fragmentContainer.id
        )
    }

    override fun getDialogHeight() = DisplayHelper.getScreenHeight()
    override fun onFinish() {
        dismiss()
        MediaPicker.listeners().clear()
    }
}