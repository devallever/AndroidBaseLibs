package app.allever.android.lib.project

import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.camerax.demo.CameraActivity
import app.allever.android.lib.common.ListFragment
import app.allever.android.lib.common.ListViewModel
import app.allever.android.lib.common.adapter.TextClickAdapter
import app.allever.android.lib.common.adapter.bean.TextClickItem
import app.allever.android.lib.common.databinding.FragmentListBinding
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toastLong
import app.allever.android.lib.core.function.businessinterceptor.demo.BusinessInterceptorActivity
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.function.media.MusicPlayer
import app.allever.android.lib.core.function.mediapicker.MediaPickerHelper
import app.allever.android.lib.core.function.mediapicker.MediaPickerResult
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.mvp.demo.MvpActivity
import app.allever.android.lib.mvvm.demo.MvvmActivity
import app.allever.android.lib.network.demo.NetworkActivity
import app.allever.android.lib.permission.permissiox.demo.PermissionXActivity
import app.allever.android.lib.widget.demo.RefreshRVActivity
import kotlinx.coroutines.launch

class MainListFragment : ListFragment<FragmentListBinding, ListViewModel, TextClickItem>() {

    private val mMusicPlayer = MusicPlayer()

    val url =
        "https://win-web-ri01-sycdn.kuwo.cn/707652b674686c7f7f5540d05f90a886/63747cc0/resource/n1/40/9/760095559.mp3"

    override fun getAdapter() = TextClickAdapter()

    override fun getList() = mutableListOf(
        TextClickItem("MVP") {
            ActivityHelper.startActivity(MvpActivity::class.java)
        },
        TextClickItem("MVVM") {
            ActivityHelper.startActivity(MvvmActivity::class.java)
        },
        TextClickItem("Network") {
            ActivityHelper.startActivity(NetworkActivity::class.java)
        },
        TextClickItem("Dialog") {
            ActivityHelper.startActivity(DialogActivity::class.java)
        },
        TextClickItem("Permission") {
            ActivityHelper.startActivity(PermissionXActivity::class.java)
        },
        TextClickItem("Camera") {
            ActivityHelper.startActivity(CameraActivity::class.java)
        },
        TextClickItem("refreshRV") {
            ActivityHelper.startActivity(RefreshRVActivity::class.java)
        },
        TextClickItem("ImageLoader") {
            ActivityHelper.startActivity(ImageLoaderActivity::class.java)
        },
        TextClickItem("BusinessInterceptor") {
            ActivityHelper.startActivity(BusinessInterceptorActivity::class.java)
        },
        TextClickItem("btnBaseActivity") {
            ActivityHelper.startActivity(UserActivity::class.java)
        },
        TextClickItem("媒体选择器(图片/视频/音频)") {
            lifecycleScope.launch {
                val result = MediaPickerHelper.launchPicker(
                    requireContext(),
                    MediaPickerHelper.TYPE_IMAGE,
                    MediaPickerHelper.TYPE_VIDEO,
                    MediaPickerHelper.TYPE_AUDIO
                )
                handleResult(result)
            }

//            MediaPicker.launchPickerDialog(
//                supportFragmentManager,
//                MediaHelper.TYPE_IMAGE,
//                MediaHelper.TYPE_VIDEO,
//                MediaHelper.TYPE_AUDIO,
//                mediaPickerListener = mediaPickerListener
//            )

            return@TextClickItem
            lifecycleScope.launch {
                val mediaFolderList = MediaHelper.getAllFolder(requireContext())
                log("媒体资源文件夹")
                mediaFolderList.map {
                    log("媒体资源文件夹: ${it.dir}")
                }
                val videoFolderList =
                    MediaHelper.getAllFolder(requireContext(), MediaHelper.TYPE_VIDEO, true)
                log("视频文件夹")
                videoFolderList.map {
                    log("视频文件夹: ${it.dir}")
                }
                val imageFolderList =
                    MediaHelper.getAllFolder(requireContext(), MediaHelper.TYPE_IMAGE, true)
                log("图片文件夹")
                imageFolderList.map {
                    log("图片文件夹: ${it.dir}")
                }

                val audioFolderList =
                    MediaHelper.getAllFolder(requireContext(), MediaHelper.TYPE_AUDIO, false)
                log("音频文件夹")
                audioFolderList.map { it ->
                    log("音频文件夹: ${it.dir}")
//                    val musicList = MediaHelper.getAudioMedia(requireContext(), it.dir, 0)
//                    musicList.map {
//                        log("music: ${it.musicTitle} - ${it.musicArtist} - ${it.musicAlbum}")
//                    }
                }
            }
        },
        TextClickItem("媒体选择器(图片/视频)") {
            lifecycleScope.launch {
                val result = MediaPickerHelper.launchPicker(
                    requireContext(),
                    MediaPickerHelper.TYPE_IMAGE,
                    MediaPickerHelper.TYPE_VIDEO
                )

                handleResult(result)
            }
        },
        TextClickItem("媒体选择器(图片)") {
            lifecycleScope.launch {
                val result =
                    MediaPickerHelper.launchPicker(requireContext(), MediaHelper.TYPE_IMAGE)
                handleResult(result)
            }
        },
        TextClickItem("图片压缩") {
            ActivityHelper.startActivity<ImageCompressActivity>()
        },
        TextClickItem("加载在线音乐") {
            mMusicPlayer.load(url)
            mMusicPlayer.onPrepareListener = {
                mMusicPlayer.play()
            }
        },
        TextClickItem("加载并播放在线音乐") {
            mMusicPlayer.load(url, true)
        }
    )

    override fun onDestroyView() {
        super.onDestroyView()
        mMusicPlayer.release()
    }

    private fun handleResult(result: MediaPickerResult) {
        val builder = StringBuilder()
        result.list.map {
            builder.append(it.path).append("\n")
            log("选中：${it.path}")
        }
        toastLong(builder.toString())
    }
}