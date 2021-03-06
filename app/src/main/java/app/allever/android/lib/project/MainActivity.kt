package app.allever.android.lib.project

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.ext.toastLong
import app.allever.android.lib.core.function.businessinterceptor.demo.BusinessInterceptorActivity
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.function.work.PollingTask
import app.allever.android.lib.core.function.work.PollingTask2
import app.allever.android.lib.core.function.work.TimerTask
import app.allever.android.lib.core.function.work.TimerTask2
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.demo.DemoMainActivity
import app.allever.android.lib.mvp.demo.MvpActivity
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.demo.MvvmActivity
import app.allever.android.lib.network.demo.NetworkActivity
import app.allever.android.lib.permission.permissiox.demo.PermissionXActivity
import app.allever.android.lib.project.databinding.ActivityMainBinding
import app.allever.android.lib.widget.demo.RefreshRVActivity
import app.allever.android.lib.widget.mediapicker.MediaPicker
import app.allever.android.lib.widget.mediapicker.MediaPickerListener
import app.allever.android.lib.widget.ripple.RippleHelper
import kotlinx.coroutines.launch

class MainActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        RippleHelper.addRippleView(findViewById(R.id.btnMvvm))
        RippleHelper.addRippleView(findViewById(R.id.btnMvp))
        RippleHelper.addRippleView(findViewById(R.id.btnNetwork))
        RippleHelper.addRippleView(findViewById(R.id.btnDialog))
        RippleHelper.addRippleView(findViewById(R.id.btnPermission))
        RippleHelper.addRippleView(findViewById(R.id.btnRefreshRV))
        RippleHelper.addRippleView(findViewById(R.id.btnImageLoader))
        RippleHelper.addRippleView(findViewById(R.id.btnBaseActivity))
        RippleHelper.addRippleView(findViewById(R.id.btnMediaSelector))
        RippleHelper.addRippleView(findViewById(R.id.btnMediaSelectorImageVideo))
        RippleHelper.addRippleView(findViewById(R.id.btnMediaSelectorImage))
        RippleHelper.addRippleView(findViewById(R.id.btnDemo))

        findViewById<View>(R.id.btnMvvm).setOnClickListener {
            ActivityHelper.startActivity(MvvmActivity::class.java)
//            ActivityHelper.startActivity(CropMainActivity::class.java)
//            ActivityHelper.startActivity(AndPermissionActivity::class.java)
        }

        findViewById<View>(R.id.btnMvp).setOnClickListener {
            ActivityHelper.startActivity(MvpActivity::class.java)
        }

        findViewById<View>(R.id.btnNetwork).setOnClickListener {
            ActivityHelper.startActivity(NetworkActivity::class.java)
        }

        findViewById<View>(R.id.btnDialog).setOnClickListener {
            ActivityHelper.startActivity(DialogActivity::class.java)
        }

        findViewById<View>(R.id.btnPermission).setOnClickListener {
//            ActivityHelper.startActivity(AndPermissionActivity::class.java)
            ActivityHelper.startActivity(PermissionXActivity::class.java)
        }

        findViewById<View>(R.id.btnRefreshRV).setOnClickListener {
            ActivityHelper.startActivity(RefreshRVActivity::class.java)
        }

        findViewById<View>(R.id.btnImageLoader).setOnClickListener {
            ActivityHelper.startActivity(ImageLoaderActivity::class.java)
        }

        findViewById<View>(R.id.btnBusinessInterceptor).setOnClickListener {
            ActivityHelper.startActivity(BusinessInterceptorActivity::class.java)
        }

        findViewById<View>(R.id.btnBaseActivity).setOnClickListener {
            ActivityHelper.startActivity(UserActivity::class.java)
        }

        val mediaPickerListener = object : MediaPickerListener {
            override fun onPicked(
                all: MutableList<MediaBean>,
                imageList: MutableList<MediaBean>,
                videoList: MutableList<MediaBean>,
                audioList: MutableList<MediaBean>
            ) {
                val builder = StringBuilder()
                all.map {
                    builder.append(it.path).append("\n")
                    log("?????????${it.path}")
                }
                toastLong(builder.toString())
            }
        }

        findViewById<View>(R.id.btnMediaSelector).setOnClickListener {

            MediaPicker.launchPickerActivity(
                MediaHelper.TYPE_IMAGE,
                MediaHelper.TYPE_VIDEO,
                MediaHelper.TYPE_AUDIO,
                mediaPickerListener = mediaPickerListener
            )
//            MediaPicker.launchPickerDialog(
//                supportFragmentManager,
//                MediaHelper.TYPE_IMAGE,
//                MediaHelper.TYPE_VIDEO,
//                MediaHelper.TYPE_AUDIO,
//                mediaPickerListener = mediaPickerListener
//            )
            return@setOnClickListener
            lifecycleScope.launch {
                val mediaFolderList = MediaHelper.getAllFolder(this@MainActivity)
                log("?????????????????????")
                mediaFolderList.map {
                    log("?????????????????????: ${it.dir}")
                }
                val videoFolderList =
                    MediaHelper.getAllFolder(this@MainActivity, MediaHelper.TYPE_VIDEO, true)
                log("???????????????")
                videoFolderList.map {
                    log("???????????????: ${it.dir}")
                }
                val imageFolderList =
                    MediaHelper.getAllFolder(this@MainActivity, MediaHelper.TYPE_IMAGE, true)
                log("???????????????")
                imageFolderList.map {
                    log("???????????????: ${it.dir}")
                }

                val audioFolderList =
                    MediaHelper.getAllFolder(this@MainActivity, MediaHelper.TYPE_AUDIO, false)
                log("???????????????")
                audioFolderList.map { it ->
                    log("???????????????: ${it.dir}")
//                    val musicList = MediaHelper.getAudioMedia(this@MainActivity, it.dir, 0)
//                    musicList.map {
//                        log("music: ${it.musicTitle} - ${it.musicArtist} - ${it.musicAlbum}")
//                    }
                }
            }
        }

        findViewById<View>(R.id.btnMediaSelectorImageVideo).setOnClickListener {
            MediaPicker.launchPickerActivity(
                MediaHelper.TYPE_IMAGE,
                MediaHelper.TYPE_VIDEO,
                mediaPickerListener = mediaPickerListener
            )
        }

        findViewById<View>(R.id.btnMediaSelectorImage).setOnClickListener {
            MediaPicker.launchPickerActivity(
                MediaHelper.TYPE_IMAGE,
                mediaPickerListener = mediaPickerListener
            )
        }

        findViewById<View>(R.id.btnDemo).setOnClickListener {
            ActivityHelper.startActivity<DemoMainActivity>()
        }


        object : TimerTask() {
            override fun delay() = 3 * 1000L
            override fun execute() = toast("?????????????????? TimerTask")
        }.start()

        TimerTask2(6000) {
//            toast("?????????????????? TimerTask2")
        }.start()

        object : PollingTask() {
            override fun interval() = 1000L
            override fun condition() = true
            override fun execute() {
                log("?????????????????? PollingTask")
//                toast("?????????????????? PollingTask")
            }
        }.start()

        PollingTask2(1000, condition = {
            true
        }) {
            log("?????????????????? PollingTask2")
//            toast("?????????????????? PollingTask2")
        }.start()


    }

    override fun onResume() {
        super.onResume()
//        toast("??????Activity = ${ActivityHelper.getTopActivity()?.javaClass?.simpleName}")
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }
}

class MainViewModel : BaseViewModel() {
    override fun init() {

    }
}