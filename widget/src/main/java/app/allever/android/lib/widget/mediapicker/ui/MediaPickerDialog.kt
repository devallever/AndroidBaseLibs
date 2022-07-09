package app.allever.android.lib.widget.mediapicker.ui

import app.allever.android.lib.core.base.dialog.AbstractSlideBottomDialog
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.helper.FragmentHelper
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.DialogMediaPickerBinding

class MediaPickerDialog(val typeList: ArrayList<String>) :
    AbstractSlideBottomDialog<DialogMediaPickerBinding>() {
    override fun getLayoutId() = R.layout.dialog_media_picker

    override fun initView() {
        FragmentHelper.addToContainer(
            childFragmentManager,
            FragmentHelper.newInstance(MediaPickerFragment::class.java) {
                putStringArrayList(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST, typeList)
            },
            mBinding.fragmentContainer.id
        )
    }

    override fun getDialogHeight() = DisplayHelper.getScreenHeight()
}