package app.allever.android.lib.widget.mediapicker

import android.content.Context
import androidx.fragment.app.FragmentManager
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toJson
import app.allever.android.lib.core.function.media.FolderBean
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.core.function.media.MediaHelper
import app.allever.android.lib.core.function.mediapicker.IMediaPickerEngine
import app.allever.android.lib.core.function.mediapicker.MediaPickerResult
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.widget.mediapicker.ui.MediaPickerActivity
import app.allever.android.lib.widget.mediapicker.ui.MediaPickerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MediaPicker: IMediaPickerEngine {

    private val mMediaPickerListenerSet = mutableListOf<MediaPickerListener>()

    /**
     * 缓存目录/全部图片/全部视频/全部音乐
     */
    val cacheFolderList = mutableListOf<FolderBean>()
    val cacheAllImageBeanList = mutableListOf<MediaBean>()
    val cacheAllVideoBeanList = mutableListOf<MediaBean>()
    val cacheAllAudioBeanList = mutableListOf<MediaBean>()

    /**
     * intent: 1MB限制
     */
    val extraMap = mutableMapOf<String, Any>()

    fun launchPickerActivity(
        vararg type: String,
        mediaPickerListener: MediaPickerListener? = null
    ) {
        mediaPickerListener?.let {
            mMediaPickerListenerSet.clear()
            mMediaPickerListenerSet.add(mediaPickerListener)
        }
        ActivityHelper.startActivity<MediaPickerActivity> {
            putStringArrayListExtra(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST, vararg2List(*type))
        }
    }

    suspend fun launchPickerActivity(
        vararg type: String
    ): PickerResult = suspendCoroutine {

        mMediaPickerListenerSet.clear()
        mMediaPickerListenerSet.add(object : MediaPickerListener {
            override fun onPicked(
                all: MutableList<MediaBean>,
                imageList: MutableList<MediaBean>,
                videoList: MutableList<MediaBean>,
                audioList: MutableList<MediaBean>
            ) {
                val result = PickerResult()
                result.all.addAll(all)
                result.imageList.addAll(imageList)
                result.videoList.addAll(videoList)
                result.audioList.addAll(audioList)
                it.resume(result)
            }
        })

        ActivityHelper.startActivity<MediaPickerActivity> {
            putStringArrayListExtra(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST, vararg2List(*type))
        }
    }


    /**
     * 弹窗方式还没做好
     */
    fun launchPickerDialog(
        fragmentManager: FragmentManager,
        vararg type: String,
        mediaPickerListener: MediaPickerListener? = null
    ) {
        mediaPickerListener?.let {
            mMediaPickerListenerSet.add(mediaPickerListener)
        }
        MediaPickerDialog(vararg2List(*type)).show(fragmentManager)
    }

    fun listeners() = mMediaPickerListenerSet

    private fun vararg2List(vararg data: String): ArrayList<String> {
        val list = ArrayList<String>()
        data.map {
            list.add(it)
        }
        return list
    }

    suspend fun fetchFolderList(context: Context, typeList: java.util.ArrayList<String>) =
        withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            val mediaFolderList = MediaHelper.getAllFolder(context) {
                log("获取全部文件夹进度: ${it.toJson()}")
            }
            var imageCount = 0
            var videoCount = 0
            var audioCount = 0
            val emptyList = mutableListOf<FolderBean>()
            mediaFolderList.map {
                if (typeList.contains(MediaHelper.TYPE_IMAGE)) {
                    val imageList = if (it.bucketId == null) {
                        MediaHelper.getImageMedia(context, it.dir) {

                        }
                    } else {
                        MediaHelper.getImageMediaBeanFromBucketId(context, it.bucketId, true) {

                        }
                    }
                    it.photoCount = imageList.size
                    if (imageList.isNotEmpty()) {
                        it.coverMediaBean = imageList[0]
                    }
                    imageCount += it.photoCount
                    it.imageMediaList.addAll(imageList)
//                MediaPicker.cacheAllImageBeanList.addAll(it.imageMediaList)
                }

                if (typeList.contains(MediaHelper.TYPE_VIDEO)) {
                    val videoList = if (it.bucketId == null) {
                        MediaHelper.getVideoMedia(context, it.dir) {

                        }
                    } else {
                        MediaHelper.getVideoMediaBeanFromBucketId(context, it.bucketId) {

                        }
                    }
                    if (videoList.isNotEmpty()) {
                        it.coverMediaBean = videoList[0]
                    }
                    it.videoCount = videoList.size
                    videoCount += it.videoCount
                    it.videoMediaList.addAll(videoList)
//                MediaPicker.cacheAllVideoBeanList.addAll(it.videoMediaList)
                }

                if (typeList.contains(MediaHelper.TYPE_AUDIO)) {
                    val audioList = if (it.bucketId == null) {
                        MediaHelper.getAudioMedia(context, it.dir) {

                        }
                    } else {
                        MediaHelper.getAudioMediaBeanFromBucketId(context, it.bucketId) {

                        }
                    }
                    it.audioCount = audioList.size
                    audioCount += it.audioCount
                    it.audioMediaList.addAll(audioList)
//                MediaPicker.cacheAllAudioBeanList.addAll(it.audioMediaList)
                }

                //暂时解决目录出现空数据也显示在列表
                if (it.audioCount <= 0 && it.videoCount <= 0 && it.photoCount <= 0) {
                    emptyList.add(it)
                }
            }

            emptyList.map {
                mediaFolderList.remove(it)
            }

            val endTime = System.currentTimeMillis()
            log("getFolder 耗时：${(endTime - startTime)}")

            val allFolderBean = FolderBean()
            mediaFolderList.mapIndexed { index, folderBean ->
                folderBean.imageMediaList.map {
                    allFolderBean.coverMediaBean = it
                    return@mapIndexed
                }
                folderBean.videoMediaList.map {
                    allFolderBean.coverMediaBean = it
                    return@mapIndexed
                }
            }
            allFolderBean.audioCount = audioCount
            allFolderBean.photoCount = imageCount
            allFolderBean.videoCount = videoCount
            allFolderBean.total = audioCount + imageCount + videoCount
            allFolderBean.name = "全部"
            mediaFolderList.add(0, allFolderBean)
            synchronized(this) {
                cacheFolderList.clear()
                cacheFolderList.addAll(mediaFolderList)
            }
            mediaFolderList
        }

    suspend fun fetchFromPhone(
        context: Context,
        mediaType: String,
        path: String = ""
    ) = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val list = when (mediaType) {
            MediaHelper.TYPE_IMAGE -> {
                MediaHelper.getImageMedia(context, path, true)
            }
            MediaHelper.TYPE_VIDEO -> {
                MediaHelper.getVideoMedia(context, path, 0)
            }
            MediaHelper.TYPE_AUDIO -> {
                MediaHelper.getAudioMedia(context, path, 0)
            }
            else -> {
                mutableListOf()
            }
        }

        //缓存
        synchronized(this) {
            if (path.isEmpty()) {
                when (mediaType) {
                    MediaHelper.TYPE_IMAGE -> {
                        cacheAllImageBeanList.clear()
                        cacheAllImageBeanList.addAll(list)
                    }
                    MediaHelper.TYPE_VIDEO -> {
                        cacheAllVideoBeanList.clear()
                        cacheAllVideoBeanList.addAll(list)
                    }
                    MediaHelper.TYPE_AUDIO -> {
                        cacheAllAudioBeanList.clear()
                        cacheAllAudioBeanList.addAll(list)
                    }
                }
            }
        }
        val endTime = System.currentTimeMillis()
        log("fetchFromPhone 耗时：${endTime - startTime}")
        generateMediaItemList(list)
    }

    suspend fun fetchFromCache(mediaType: String, path: String) = withContext(Dispatchers.IO) {
        val result = mutableListOf<MediaItem>()
        val isAll = path.isEmpty()
        if (isAll) {
            if (mediaType == MediaHelper.TYPE_IMAGE) {
                if (cacheAllImageBeanList.isNotEmpty()) {
                    result.addAll(generateMediaItemList(cacheAllImageBeanList))
                    return@withContext result
                }
            } else if (mediaType == MediaHelper.TYPE_VIDEO) {
                if (cacheAllVideoBeanList.isNotEmpty()) {
                    result.addAll(generateMediaItemList(cacheAllVideoBeanList))
                    return@withContext result
                }
            } else if (mediaType == MediaHelper.TYPE_AUDIO) {
                if (cacheAllAudioBeanList.isNotEmpty()) {
                    result.addAll(generateMediaItemList(cacheAllAudioBeanList))
                    return@withContext result
                }
            }
        }
        result
    }

    suspend fun fetchFromFolderCache(mediaType: String, path: String) =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<MediaItem>()
            var folderBean: FolderBean? = null
            if (path.isNotEmpty()) {
                cacheFolderList.map {
                    if (it.dir == path) {
                        folderBean = it
                        return@map
                    }
                }
                folderBean?.let {
                    if (mediaType == MediaHelper.TYPE_IMAGE) {
                        result.addAll(generateMediaItemList(it.imageMediaList))
                        if (result.isNotEmpty()) {
                            return@withContext result
                        }
                    } else if (mediaType == MediaHelper.TYPE_VIDEO) {
                        result.addAll(generateMediaItemList(it.videoMediaList))
                        if (result.isNotEmpty()) {
                            return@withContext result
                        }
                    } else if (mediaType == MediaHelper.TYPE_AUDIO) {
                        result.addAll(generateMediaItemList(it.audioMediaList))
                        if (result.isNotEmpty()) {
                            return@withContext result
                        }
                    }
                }
            }

            result
        }

    private suspend fun generateMediaItemList(list: MutableList<MediaBean>): Collection<MediaItem> =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<MediaItem>()
            list.map {
                val mediaItem = MediaItem(it)
                result.add(mediaItem)
            }
            result
        }


    override suspend fun launchPicker(context: Context, vararg type: String): MediaPickerResult {
        return suspendCoroutine {
            mMediaPickerListenerSet.clear()
            mMediaPickerListenerSet.add(object : MediaPickerListener {
                override fun onPicked(
                    all: MutableList<MediaBean>,
                    imageList: MutableList<MediaBean>,
                    videoList: MutableList<MediaBean>,
                    audioList: MutableList<MediaBean>
                ) {
                    val result = MediaPickerResult()
                    result.list.addAll(all)
                    it.resume(result)
                }
            })

            ActivityHelper.startActivity<MediaPickerActivity> {
                putStringArrayListExtra(MediaPickerActivity.EXTRA_MEDIA_TYPE_LIST, vararg2List(*type))
            }
        }
    }
}