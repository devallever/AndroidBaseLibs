package app.allever.android.lib.camerax.demo

import android.graphics.Bitmap
import app.allever.android.lib.camerax.databinding.FragmentCameraXBinding
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.camera.CameraListener
import app.allever.android.lib.core.function.camera.CameraManager
import app.allever.android.lib.mvvm.base.BaseMvvmFragment
import app.allever.android.lib.mvvm.base.BaseViewModel
import java.io.File

class CameraXFragment : BaseMvvmFragment<FragmentCameraXBinding, CameraXViewModel>() {

    override fun inflate() = FragmentCameraXBinding.inflate(layoutInflater)

    override fun init() {
        CameraManager.setCameraListener(object : CameraListener {
            override fun onPreview(data: ByteArray, imageFormat: Int) {
            }

            override fun onTakePicture(data: ByteArray?, bitmap: Bitmap?, imageFormat: Int) {
                val path =
                    requireActivity().externalCacheDir?.absolutePath + File.separator + System.currentTimeMillis() + ".jpg"
                val result = CameraManager.saveBitmap2File(
                    bitmap,
                    path
                )
                if (result) {
                    log("保存成功：$path")
                } else {
                    log("保存失败")
                }
            }
        })

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
            CameraManager.setLifeCycleOwner(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraManager.closeCamera()
        CameraManager.release()
    }
}

class CameraXViewModel() : BaseViewModel() {
    override fun init() {

    }
}