package app.allever.android.lib.camerax.demo

import androidx.fragment.app.Fragment
import app.allever.android.lib.camerax.Camera2ProxyImpl
import app.allever.android.lib.camerax.CameraProxyImpl
import app.allever.android.lib.camerax.CameraXProxyImpl
import app.allever.android.lib.common.TabActivity
import app.allever.android.lib.common.TabViewModel
import app.allever.android.lib.common.databinding.ActivityTabBinding
import app.allever.android.lib.core.function.camera.CameraManager

class CameraActivity : TabActivity<ActivityTabBinding, TabViewModel>() {
    override fun getPageTitle() = "Camera"

    override fun getTabTitles() = mutableListOf("Camera", "Camera2", "CameraX")

    override fun getFragments(): MutableList<Fragment> =
        mutableListOf(CameraFragment(), Camera2Fragment(), CameraXFragment())

    override fun onPageChanged(position: Int) {
        CameraManager.closeCamera()
        CameraManager.release()
        when (position) {
            0 -> {
                CameraManager.injectProxy(CameraProxyImpl())
            }
            1 -> {
                CameraManager.injectProxy(Camera2ProxyImpl())
            }
            2 -> {
                CameraManager.injectProxy(CameraXProxyImpl())
            }
        }
    }

    override fun inflateChildBinding() = ActivityTabBinding.inflate(layoutInflater)

}