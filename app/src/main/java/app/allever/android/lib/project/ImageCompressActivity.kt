package app.allever.android.lib.project

import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.imagecompress.ImageCompress
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.function.mediapicker.MediaPickerHelper
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.project.databinding.ActivityImageCompressBinding
import app.allever.android.lib.widget.mediapicker.MediaPickerListener
import kotlinx.coroutines.launch

class ImageCompressActivity : BaseActivity<ActivityImageCompressBinding, ImageCompressViewModel>() {

    override fun inflateChildBinding(): ActivityImageCompressBinding = ActivityImageCompressBinding.inflate(layoutInflater)

    override fun init() {
        initTopBar("图片压缩")
        val pathList = mutableListOf<String>()

        val mediaPickerListener = object : MediaPickerListener {
            override fun onPicked(
                all: MutableList<MediaBean>,
                imageList: MutableList<MediaBean>,
                videoList: MutableList<MediaBean>,
                audioList: MutableList<MediaBean>
            ) {
                val builder = StringBuilder()
                pathList.clear()
                all.map {
                    pathList.add(it.path)
                    builder.append(it.path).append("\n")
                    log("选中：${it.path}")
                }
                binding.tvSelectPhotoList.text = builder.toString()
            }
        }

        binding.btnSelectPhoto.setOnClickListener {
            lifecycleScope.launch {
                val result = MediaPickerHelper.launchPicker(this@ImageCompressActivity, MediaPickerHelper.TYPE_IMAGE)
                val builder = StringBuilder()
                pathList.clear()
                result.list.map {
                    pathList.add(it.path)
                    builder.append(it.path).append("\n")
                    log("选中：${it.path}")
                }
                binding.tvSelectPhotoList.text = builder.toString()
            }

        }

        binding.btnCompress.setOnClickListener {
            lifecycleScope.launch {
                val result = ImageCompress.compress(this@ImageCompressActivity, pathList)
                val builder = StringBuilder()
                result.list.map {
                    builder.append(it).append("\n")
                    log("压缩：${it}")
                }
                binding.tvCompressPhotoList.text = builder.toString()
            }
        }
    }

}

class ImageCompressViewModel : BaseViewModel() {
    override fun init() {

    }

}