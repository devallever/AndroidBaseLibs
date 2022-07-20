package app.allever.android.lib.core.function.media

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT
import android.media.ThumbnailUtils.extractThumbnail
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.StringDef
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt


object MediaHelper {

    private val TAG = MediaHelper::class.java.simpleName

    const val TYPE_IMAGE = "TYPE_IMAGE"
    const val TYPE_VIDEO = "TYPE_VIDEO"
    const val TYPE_AUDIO = "TYPE_AUDIO"
    const val TYPE_ALL = ""

    @StringDef(value = [TYPE_IMAGE, TYPE_VIDEO, TYPE_AUDIO, TYPE_ALL])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Type

    private const val COLUMN_BUCKET_ID = "bucket_id"
    private const val COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name"
    private const val COLUMN_DATE_TAKEN = "datetaken"
    private const val COLUMN_DATA = "_data"
    private const val COLUMN_URI = "uri"
    private const val COLUMN_COUNT = "count"
    private val QUERY_URI = MediaStore.Files.getContentUri("external")

    private val COLUMNS = arrayOf(
        MediaStore.Files.FileColumns._ID,
        COLUMN_BUCKET_ID,
        COLUMN_BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
        COLUMN_URI,
        COLUMN_COUNT
    )

    private val PROJECTION_29 = arrayOf(
        MediaStore.Files.FileColumns._ID,
        COLUMN_BUCKET_ID,
        COLUMN_BUCKET_DISPLAY_NAME,
        COLUMN_DATE_TAKEN,
        COLUMN_DATA,
        MediaStore.MediaColumns.MIME_TYPE
    )

    // === params for showSingleMediaType: false ===
    private const val SELECTION = (
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id")
    private const val SELECTION_29 = (
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0")
    private val SELECTION_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString()
    )

    private val SELECTION_ARGS_IMAGE = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
    )

    private val SELECTION_ARGS_VIDEO = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )

    private val SELECTION_ARGS_AUDIO = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString()
    )
    // =============================================

    // === params for showSingleMediaType: true ===
    private const val SELECTION_FOR_SINGLE_MEDIA_TYPE = (
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id")
    private const val SELECTION_FOR_SINGLE_MEDIA_TYPE_29 = (
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0")

    private const val SELECTION_FOR_MEDIA_TYPE = (
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0")

    private fun getSelectionArgsForSingleMediaType(mediaType: Int): Array<String> {
        return arrayOf(mediaType.toString())
    }
    // =============================================

    // === params for showSingleMediaType: true ===
    private const val SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE = (
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?"
                    + ") GROUP BY (bucket_id")
    private const val SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29 = (
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?")

    private fun getSelectionArgsForSingleMediaGifType(mediaType: Int): Array<String> {
        return arrayOf(mediaType.toString(), "image/gif")
    }
    // =============================================

    private const val BUCKET_ORDER_BY = "$COLUMN_DATE_TAKEN DESC"

    private val IMAGE_REJECTION_ARRAY = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DATE_TAKEN,
        MediaStore.Images.ImageColumns.ORIENTATION,
        MediaStore.Images.ImageColumns.WIDTH,
        MediaStore.Images.ImageColumns.HEIGHT
    )

    private val VIDEO_REJECTION_ARRAY = arrayOf(
        MediaStore.Video.VideoColumns._ID,
        MediaStore.Video.VideoColumns.DATA,
        MediaStore.Video.VideoColumns.DATE_TAKEN,
        MediaStore.Video.VideoColumns.DURATION,
        MediaStore.Video.VideoColumns.WIDTH,
        MediaStore.Video.VideoColumns.HEIGHT
    )

    private val AUDIO_REJECTION_ARRAY = arrayOf(
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DATE_ADDED,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.ALBUM,
        MediaStore.Audio.AudioColumns.ALBUM_ID
    )

    fun getTypeName(@Type type: String): String {
        when (type) {
            TYPE_IMAGE -> return "图片"
            TYPE_VIDEO -> return "视频"
            TYPE_AUDIO -> return "歌曲"
        }
        return "其他"
    }

    /**
     * 获取所有文件夹
     */
    suspend fun getAllFolder(
        context: Context,
        @Type type: String = TYPE_ALL,
        includeGif: Boolean = false
    ) = withContext(Dispatchers.IO) {

        val imageFolderList = mutableListOf<FolderBean>()
        var cursor: Cursor? = null
        try {
            val contentResolver = App.context.contentResolver

            val selectionArgs = when (type) {
                TYPE_IMAGE -> SELECTION_ARGS_IMAGE
                TYPE_VIDEO -> SELECTION_ARGS_VIDEO
                TYPE_AUDIO -> SELECTION_ARGS_AUDIO
                else -> SELECTION_ARGS
            }

            val selection = if (type.isEmpty()) {
                SELECTION_FOR_MEDIA_TYPE
            } else {
                SELECTION_FOR_SINGLE_MEDIA_TYPE_29
            }

            cursor = contentResolver.query(
                QUERY_URI,
                PROJECTION_29,
                selection,
                selectionArgs,
                BUCKET_ORDER_BY
            )

            // Pseudo GROUP BY
            val countMap = HashMap<Long, Long>()
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val bucketId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUCKET_ID))

                    var count = countMap[bucketId]
                    if (count == null) {
                        count = 1L
                    } else {
                        count++
                    }
                    countMap[bucketId] = count
                }
            }

            if (cursor != null) {
                if (cursor.moveToFirst()) {

                    val done = HashSet<Long>()

                    do {
                        val bucketId = cursor.getLong(cursor.getColumnIndex(COLUMN_BUCKET_ID))
                        log(TAG, "bucketId = $bucketId")
                        if (done.contains(bucketId)) {
                            continue
                        }

                        val fileId = cursor.getLong(
                            cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                        )
                        log(TAG, "id = $fileId")
                        val bucketDisplayName = cursor.getString(
                            cursor.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME)
                        )
                        log(TAG, "displayName = $bucketDisplayName")
                        val mimeType = cursor.getString(
                            cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
                        )
                        log(TAG, "mimeType = $mimeType")
                        val uri = getUri(cursor)
                        log(TAG, "uri = $uri")
                        val count = countMap[bucketId]!!
                        log(TAG, "count = $count")
                        val date = cursor.getLong(
                            cursor.getColumnIndex(COLUMN_DATE_TAKEN)
                        )
                        log(TAG, "date = $date")
                        val filePath = cursor.getString(
                            cursor.getColumnIndex(COLUMN_DATA)
                        )
                        val path =
                            filePath.substring(0, filePath.lastIndexOf(File.separatorChar))
                        log(TAG, "path = $path")

                        //-----------------------------------------
                        val imageFolder = FolderBean()
                        imageFolder.total = count.toInt()
                        val bean = MediaBean()
                        bean.date = date
                        bean.path = filePath

                        bean.uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            fileId
                        )
                        if (bucketDisplayName == null) {
                            imageFolder.setDirAndName(path)
                        } else {
                            imageFolder.dir = path
                            imageFolder.name = bucketDisplayName
                        }
                        imageFolder.bucketId = bucketId.toString()

                        if (type == TYPE_IMAGE) {
                            when {
                                MediaFile.isGifFileType(bean.path) -> bean.type =
                                    MediaType.TYPE_GIF
                                MediaFile.isJPGFileType(bean.path) -> bean.type =
                                    MediaType.TYPE_JPG
                                MediaFile.isPNGFileType(bean.path) -> bean.type =
                                    MediaType.TYPE_PNG
                                else -> bean.type = MediaType.TYPE_OTHER_IMAGE
                            }

                            if (!includeGif) {
                                val imgExcludeGif =
                                    getImageMedia(
                                        context,
                                        path, false
                                    )
                                if (imgExcludeGif.size > 0) {
                                    imageFolder.coverMediaBean = (imgExcludeGif[0])
                                    imageFolder.total = imgExcludeGif.size
                                    imageFolderList.add(imageFolder)
                                }
                            } else {
                                imageFolder.coverMediaBean = bean
                                imageFolderList.add(imageFolder)
                            }
                        } else if (type == TYPE_VIDEO) {
                            bean.type = MediaType.TYPE_VIDEO
                            imageFolder.coverMediaBean = bean
                            imageFolderList.add(imageFolder)
                        } else if (type == TYPE_AUDIO) {
                            bean.type = MediaType.TYPE_AUDIO
                            imageFolderList.add(imageFolder)
                        } else {
                            imageFolderList.add(imageFolder)
                        }

                        done.add(bucketId)

                        log(TAG, "-----------------------------------------\n\n")
                    } while (cursor.moveToNext())
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        imageFolderList
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param localPath
     * @return
     */
    suspend fun getLocalVideoBitmap(uri: Uri?) = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(App.context, uri)
            //获得第一帧图片
            bitmap = retriever.frameAtTime
        } catch (e: java.lang.IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return@withContext bitmap
    }

    suspend fun getImageMedia(context: Context, path: String, includeGif: Boolean = true) =
        withContext(Dispatchers.IO) {
            val result: MutableList<MediaBean> = mutableListOf()
            val cursor: Cursor? = if (TextUtils.isEmpty(path)) {
                generateImageCursor(context, null, null)
            } else {
                generateImageCursor(
                    context,
                    MediaStore.Images.ImageColumns.DATA + " like ? ",
                    arrayOf(path + File.separator + "%")
                )
            }
            result.addAll(generateImageResult(cursor, includeGif))
            result
        }

    suspend fun getVideoMedia(
        context: Context,
        path: String,
        maxDuration: Long = 0
    ) = withContext(Dispatchers.IO) {
        val result: MutableList<MediaBean> = mutableListOf()
        val cursor = if (TextUtils.isEmpty(path)) {
            generateVideoCursor(context, null, null)
        } else {
            generateVideoCursor(
                context,
                MediaStore.Video.VideoColumns.DATA + " like ? ",
                arrayOf(path + File.separator + "%")
            )
        }
        result.addAll(generateVideoResult(cursor, maxDuration))
        result
    }

    suspend fun getAudioMedia(
        context: Context,
        path: String,
        maxDuration: Long = 0
    ) = withContext(Dispatchers.IO) {
        val result: MutableList<MediaBean> = mutableListOf()
        val cursor = if (TextUtils.isEmpty(path)) {
            generateAudioCursor(context, null, null)
        } else {
            generateAudioCursor(
                context,
                MediaStore.Audio.AudioColumns.DATA + " like ? ",
                arrayOf(path + File.separator + "%")
            )
        }
        result.addAll(generateAudioResult(cursor, maxDuration))
        result
    }

    /**
     * 获取某个文件夹下的所有图片/视频/音频
     *
     * @param context
     * @param bucketId
     * @return
     */
    suspend fun getAllMediaBeanFromBucketId(
        context: Context,
        bucketId: String?,
        maxDuring: Long
    ) = withContext(Dispatchers.IO) {
        val imageList: ArrayList<MediaBean> = getImageMediaBeanFromBucketId(context, bucketId)
        val videoList: ArrayList<MediaBean> =
            getVideoMediaBeanFromBucketId(context, bucketId, maxDuring)
        val audioList: ArrayList<MediaBean> = getAudioMediaBeanFromBucketId(context, bucketId)
        val imageAndVideoList = doMediaBeanAlgorithm(imageList, videoList)
        return@withContext doMediaBeanAlgorithm(imageAndVideoList, audioList)
    }

    /**
     * 获取某个文件夹下的所有图片
     *
     * @param context
     * @param bucketId
     * @return
     */
    suspend fun getImageMediaBeanFromBucketId(
        context: Context,
        bucketId: String?,
        includeGif: Boolean = true
    ) = withContext(Dispatchers.IO) {
        val result: ArrayList<MediaBean> = ArrayList()
        if (TextUtils.isEmpty(bucketId)) {
            return@withContext result
        }
        val cursor = generateImageCursor(
            context,
            MediaStore.Images.ImageColumns.BUCKET_ID + " = ? ",
            arrayOf(bucketId)
        )
        result.addAll(generateImageResult(cursor, includeGif))
        return@withContext result
    }

    /**
     * 获取某个文件夹下的视频
     *
     * @param context
     * @param bucketId
     * @return
     */
    suspend fun getVideoMediaBeanFromBucketId(
        context: Context,
        bucketId: String?,
        maxDuration: Long = 0L
    ) = withContext(Dispatchers.IO) {
        val result: ArrayList<MediaBean> = ArrayList()
        if (TextUtils.isEmpty(bucketId)) {
            return@withContext result
        }
        val cursor =
            generateVideoCursor(
                context,
                MediaStore.Video.VideoColumns.BUCKET_ID + " = ? ",
                arrayOf(bucketId)
            )
        result.addAll(generateVideoResult(cursor, maxDuration))
        return@withContext result
    }

    /**
     * 获取某个文件夹下的音频
     *
     * @param context
     * @param bucketId
     * @return
     */
    suspend fun getAudioMediaBeanFromBucketId(
        context: Context,
        bucketId: String?,
        maxDuration: Long = 0L
    ) = withContext(Dispatchers.IO) {
        val result: ArrayList<MediaBean> = ArrayList()
        if (TextUtils.isEmpty(bucketId)) {
            return@withContext result
        }
        val cursor =
            generateAudioCursor(
                context,
                MediaStore.Audio.AudioColumns.BUCKET_ID + " = ? ",
                arrayOf(bucketId)
            )
        result.addAll(generateAudioResult(cursor, maxDuration))
        return@withContext result
    }

    private fun generateImageCursor(
        context: Context,
        selection: String? = null,
        selectionArgsArray: Array<String?>? = null
    ): Cursor? {
        val contentResolver = context.contentResolver
        return contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_REJECTION_ARRAY,
            selection,
            selectionArgsArray,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Images.ImageColumns._ID + " ASC"
        )
    }

    private fun generateVideoCursor(
        context: Context,
        selection: String? = null,
        selectionArgsArray: Array<String?>? = null
    ): Cursor? {
        val contentResolver = context.contentResolver
        return contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            VIDEO_REJECTION_ARRAY,
            selection,
            selectionArgsArray,
            MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Video.VideoColumns._ID + " ASC"
        )
    }

    private fun generateAudioCursor(
        context: Context,
        selection: String? = null,
        selectionArgsArray: Array<String?>? = null
    ): Cursor? {
        val contentResolver = context.contentResolver
        return contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            AUDIO_REJECTION_ARRAY,
            selection,
            selectionArgsArray,
            MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC" + ", " + MediaStore.Audio.AudioColumns._ID + " ASC"
        )
    }

    private suspend fun generateImageResult(cursor: Cursor?, includeGif: Boolean) =
        withContext(Dispatchers.IO) {
            val result: MutableList<MediaBean> = mutableListOf()
            if (cursor == null) {
                return@withContext result
            }
            try {
                if (cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                    val pathIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    val dateIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
                    val degreeIndex =
                        cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION)
                    val widthIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)
                    val heightIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)
                    do {
                        val mediaBean = MediaBean()
                        mediaBean.uri = (
                                ContentUris.withAppendedId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    cursor.getInt(idIndex).toLong()
                                ))
                        val thumbPath = cursor.getString(pathIndex)

                        if (checkError(TYPE_IMAGE, thumbPath)) {
                            continue
                        }
                        mediaBean.path = (thumbPath)
                        mediaBean.date = (cursor.getLong(dateIndex))
                        mediaBean.degree = (cursor.getInt(degreeIndex))
                        mediaBean.width = cursor.getInt(widthIndex)
                        mediaBean.height = cursor.getInt(heightIndex)
                        when {
                            MediaFile.isGifFileType(mediaBean.path) -> {
                                mediaBean.type = (MediaType.TYPE_GIF)
                                if (!includeGif) {
                                    continue
                                }
                            }
                            MediaFile.isJPGFileType(mediaBean.path) -> {
                                mediaBean.type = (MediaType.TYPE_JPG)
                            }
                            MediaFile.isPNGFileType(mediaBean.path) -> {
                                mediaBean.type = (MediaType.TYPE_PNG)
                            }
                            else -> {
                                mediaBean.type = (MediaType.TYPE_OTHER_IMAGE)
                            }
                        }
                        result.add(mediaBean)
                    } while (cursor.moveToNext())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor.close()
            }

            result
        }

    private suspend fun generateVideoResult(cursor: Cursor?, maxDuration: Long = 0L) =
        withContext(Dispatchers.IO) {
            val result: MutableList<MediaBean> = mutableListOf()
            try {
                if (cursor == null) {
                    return@withContext result
                }
                if (cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID)
                    val pathIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
                    val dateIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN)
                    val durationIndex =
                        cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                    val widthIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH)
                    val heightIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT)
                    do {
                        val mediaBean = MediaBean()
                        mediaBean.uri = (
                                ContentUris.withAppendedId(
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    cursor.getInt(idIndex).toLong()
                                )
                                )
                        val videoPath = cursor.getString(pathIndex)
                        if (checkError(TYPE_VIDEO, videoPath)) {
                            continue
                        }
                        mediaBean.path = (videoPath)
                        mediaBean.date = (cursor.getLong(dateIndex))
                        mediaBean.type = (MediaType.TYPE_VIDEO)
                        mediaBean.width = cursor.getInt(widthIndex)
                        mediaBean.height = cursor.getInt(heightIndex)
//                        log("video size = ${mediaBean.width} x ${mediaBean.height}")
//                        try {
//                            mediaBean.videoThumbnail =
//                                createVideoThumbnail(App.context, mediaBean, MINI_KIND)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }

                        //有些文件后缀为视频格式，却不是视频文件，长度为0， 需要排除
                        val time = cursor.getLong(durationIndex)
                        if (time <= 0) {
                            continue
                        }
                        mediaBean.duration = (time)
                        if (maxDuration <= 0 || time <= maxDuration) {
                            result.add(mediaBean)
                        }
                    } while (cursor.moveToNext())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }

            result
        }

    private suspend fun generateAudioResult(cursor: Cursor?, maxDuration: Long = 0L) =
        withContext(Dispatchers.IO) {
            val result: MutableList<MediaBean> = mutableListOf()
            try {
                if (cursor == null) {
                    return@withContext result
                }
                if (cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)
                    val pathIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                    val dateIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED)
                    val durationIndex =
                        cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
                    val titleIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
                    val artistIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
                    val albumIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)
                    val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)
                    do {
                        val mediaBean = MediaBean()
                        mediaBean.uri = (
                                ContentUris.withAppendedId(
                                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                    cursor.getInt(idIndex).toLong()
                                )
                                )
                        val audioPath = cursor.getString(pathIndex)
                        if (checkError(TYPE_AUDIO, audioPath)) {
                            continue
                        }
                        mediaBean.path = (audioPath)
                        mediaBean.date = (cursor.getLong(dateIndex))
                        mediaBean.type = (MediaType.TYPE_AUDIO)
                        mediaBean.musicTitle = cursor.getString(titleIndex)
                        mediaBean.musicArtist = cursor.getString(artistIndex)
                        mediaBean.musicAlbum = cursor.getString(albumIndex)
                        val albumId = cursor.getLong(albumIdIndex)
                        mediaBean.musicCoverUri = getMediaStoreAlbumCoverUri(albumId)
                        log("music cover uri = ${mediaBean.musicCoverUri}")
                        val inputStream = try {
                            mediaBean.musicCoverUri?.let {
                                App.context.contentResolver.openInputStream(
                                    it
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }

                        val option = BitmapFactory.Options()
                        option.inPreferredConfig = Bitmap.Config.RGB_565
                        mediaBean.musicCoverBitmap = BitmapFactory.decodeStream(inputStream)


                        //有些文件后缀为视频格式，却不是视频文件，长度为0， 需要排除
                        val time = cursor.getLong(durationIndex)
                        if (time <= 0) {
                            continue
                        }
                        mediaBean.duration = (time)
                        if (maxDuration <= 0 || time <= maxDuration) {
                            result.add(mediaBean)
                        }
                    } while (cursor.moveToNext())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }

            result
        }

    private fun checkError(type: String, path: String): Boolean {
        return if (!FileUtils.isExistsFile(path)) {
            true
        } else when (type) {
            TYPE_IMAGE -> return !MediaFile.isImageFile(path)
            TYPE_VIDEO -> return !MediaFile.isVideoFile(path)
            TYPE_AUDIO -> return !MediaFile.isAudioFile(path)
            else -> return true
        }
    }

    private fun getUri(cursor: Cursor): Uri {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
        val mimeType = cursor.getString(
            cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
        )

        val contentUri: Uri = when {
            MimeType.isImage(mimeType) -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MimeType.isVideo(mimeType) -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MimeType.isAudio(mimeType) -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> {
                MediaStore.Files.getContentUri("external")
            }
        }

        return ContentUris.withAppendedId(contentUri, id)
    }

    /**
     * 排序算法
     *
     * @param rs1
     * @param rs2
     * @return
     */
    private suspend fun doMediaBeanAlgorithm(
        rs1: java.util.ArrayList<MediaBean>?,
        rs2: java.util.ArrayList<MediaBean>?
    ) = withContext(Dispatchers.IO) {
        var i = 0
        var j = 0
        var k = 0
        val length1 = rs1?.size ?: 0
        val length2 = rs2?.size ?: 0
        val length = length1 + length2
        val result: ArrayList<MediaBean> = ArrayList(length)
        if (length1 == 0) {
            return@withContext rs2
        } else if (length2 == 0) {
            return@withContext rs1
        }
        while (k < length) {
            if (i == length1) { //rs1已经弄完
                result.add(rs2!![j])
                j++
            } else if (j == length2) { //rs2已经弄完
                result.add(rs1!![i])
                i++
            } else {
                if (rs1!![i].date > rs2!![j].date) { //rs1的放入
                    result.add(rs1[i])
                    i++
                } else { //rs2的放入
                    result.add(rs2[j])
                    j++
                }
            }
            k++
        }
        return@withContext result
    }

    private fun createVideoThumbnail(context: Context, mediaBean: MediaBean, kind: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, mediaBean.uri)
            return retriever.getFrameAtTime(1000)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            // Assume this is a corrupt video file
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release()
            } catch (ex: RuntimeException) {
                // Ignore failures while cleaning up.
            }
        }
        if (bitmap == null) return null
        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            val width = bitmap.width
            val height = bitmap.height
            val max = width.coerceAtLeast(height)
            if (max > 512) {
                val scale = 512f / max
                val w = (scale * width).roundToInt()
                val h = (scale * height).roundToInt()
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true)
            }
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = extractThumbnail(
                bitmap,
                mediaBean.width / 10,
                mediaBean.height / 10,
                OPTIONS_RECYCLE_INPUT
            )
        }
        return bitmap
    }

    private fun getMediaStoreAlbumCoverUri(albumId: Long): Uri? {
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(artworkUri, albumId)
    }
}