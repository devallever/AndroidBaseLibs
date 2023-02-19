package app.allever.android.lib.camerax.demo

import android.graphics.Bitmap
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.camerax.databinding.FragmentCameraBinding
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.camera.CameraListener
import app.allever.android.lib.core.function.camera.CameraManager
import app.allever.android.lib.core.function.imageloader.load
import app.allever.android.lib.mvvm.base.BaseMvvmFragment
import app.allever.android.lib.mvvm.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CameraFragment : BaseMvvmFragment<FragmentCameraBinding, CameraViewModel>() {

    override fun inflate() = FragmentCameraBinding.inflate(layoutInflater)

    override fun init() {
        mBinding.btnOpenFrontCamera.setOnClickListener {
            CameraManager.openCamera(1)
        }

        mBinding.btnOpenCamera.setOnClickListener {
            CameraManager.openCamera()
        }

        mBinding.btnCloseCamera.setOnClickListener {
            CameraManager.closeCamera()
        }

        mBinding.btnTackPicture.setOnClickListener {
            CameraManager.takePicture()
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.surfaceView.post {
            CameraManager.setPreview(mBinding.surfaceView)
        }
        CameraManager.setCameraListener(object : CameraListener {
            override fun onPreview(data: ByteArray, imageFormat: Int) {
            }

            override fun onTakePicture(data: ByteArray?, bitmap: Bitmap?, imageFormat: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val path =
                        requireActivity().externalCacheDir?.absolutePath + File.separator + System.currentTimeMillis() + ".jpg"
                    val result = CameraManager.saveBitmap2File(
                        bitmap,
                        path
                    )
                    val msg = if (result) {
                        "保存成功：$path"
                    } else {
                        "保存失败"
                    }
                    toast(msg)
                    log(msg)
                    launch(Dispatchers.Main) {
                        mBinding.ivPreviewPic.load(path)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraManager.closeCamera()
        CameraManager.release()
    }
}

class CameraViewModel() : BaseViewModel() {
    override fun init() {

    }
}