package app.allever.android.lib.core.function.media

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.StringDef
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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
                    + " OR " +  MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR " +  MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
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

    /**
     * 获取所有文件夹
     */
    suspend fun getAllFolder(context: Context, @Type type: String = TYPE_ALL, includeGif: Boolean = false) =
        withContext(Dispatchers.IO) {

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
                                        getImageMediaBeanFromPathExcludeGif(
                                            context,
                                            path
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
     * 获取某个文件夹下的所有图片的URI
     *
     * @param context
     * @param path
     * @return
     */
    suspend fun getImageMediaBeanFromPathExcludeGif(
        context: Context,
        path: String
    ) = withContext(Dispatchers.IO) {
        var result: MutableList<MediaBean> = mutableListOf()
        var cursor: Cursor? = null
        //Images.ImageColumns.DATA 是图片的绝对路径
        try {
            val contentResolver = context.contentResolver
            result = ArrayList<MediaBean>()
            cursor = if (TextUtils.isEmpty(path)) {
                contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.ORIENTATION
                    ),
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Images.ImageColumns._ID + " ASC"
                )
            } else {
                contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.ORIENTATION
                    ),
                    MediaStore.Images.ImageColumns.DATA + " like ? ",
                    arrayOf(path + File.separator + "%"),
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Images.ImageColumns._ID + " ASC"
                )
            }
            if (cursor?.moveToFirst() == true) {
                val idIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                val pathIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                val dateIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
                val degreeIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION)
                do {
                    val mediaBean = MediaBean()
                    mediaBean.uri = (
                            ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                cursor.getInt(idIndex).toLong()
                            )
                            )
                    mediaBean.path = (cursor.getString(pathIndex))
                    mediaBean.date = (cursor.getLong(dateIndex))
                    mediaBean.degree = (cursor.getInt(degreeIndex))
                    if (MediaFile.isGifFileType(mediaBean.path)) {
                        continue
                    } else if (MediaFile.isJPGFileType(mediaBean.path)) {
                        mediaBean.type = (MediaType.TYPE_JPG)
                    } else if (MediaFile.isPNGFileType(mediaBean.path)) {
                        mediaBean.type = (MediaType.TYPE_PNG)
                    } else {
                        mediaBean.type = (MediaType.TYPE_OTHER_IMAGE)
                    }
                    result.add(mediaBean)
                } while (cursor.moveToNext())
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        result
    }

    suspend fun getImageMedia(context: Context, path: String) = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        val result: MutableList<MediaBean> = mutableListOf()
        var cursor: Cursor? = null
        try {
            cursor = if (TextUtils.isEmpty(path)) {
                contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.ORIENTATION
                    ),
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Images.ImageColumns._ID + " ASC"
                )
            } else {
                contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.ORIENTATION
                    ),
                    MediaStore.Images.ImageColumns.DATA + " like ? ",
                    arrayOf(path + File.separator + "%"),
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Images.ImageColumns._ID + " ASC"
                )
            }
            if (cursor == null) {
                return@withContext result
            }
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                val pathIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                val dateIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
                val degreeIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION)
                do {
                    val mediaBean = MediaBean()
                    mediaBean.uri = (
                            ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                cursor.getInt(idIndex).toLong()
                            )
                            )
                    val thumbPath = cursor.getString(pathIndex)
//                    log(TAG, "Image: $thumbPath")

                    if (checkImageError(thumbPath)) {
                        continue
                    }
                    mediaBean.path = (thumbPath)
                    mediaBean.date = (cursor.getLong(dateIndex))
                    mediaBean.degree = (cursor.getInt(degreeIndex))
                    when {
                        MediaFile.isGifFileType(mediaBean.path) -> {
                            mediaBean.type = (MediaType.TYPE_GIF)
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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        result
    }

    suspend fun getVideoMedia(
        context: Context,
        path: String,
        maxDuration: Long = 0
    ) = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        val result: MutableList<MediaBean> = mutableListOf()
        var cursor: Cursor? = null
        try {
            cursor = if (TextUtils.isEmpty(path)) {
                contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Video.VideoColumns._ID,
                        MediaStore.Video.VideoColumns.DATA,
                        MediaStore.Video.VideoColumns.DATE_TAKEN,
                        MediaStore.Video.VideoColumns.DURATION
                    ),
                    null,
                    null,
                    MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Video.VideoColumns._ID + " ASC"
                )
            } else {
                contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Video.VideoColumns._ID,
                        MediaStore.Video.VideoColumns.DATA,
                        MediaStore.Video.VideoColumns.DATE_TAKEN,
                        MediaStore.Video.VideoColumns.DURATION
                    ),
                    MediaStore.Video.VideoColumns.DATA + " like ? ",
                    arrayOf(path + File.separator + "%"),
                    MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Video.VideoColumns._ID + " ASC"
                )
            }
            if (cursor == null) {
                return@withContext result
            }
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID)
                val pathIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
                val dateIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN)
                val durationIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                do {
                    val mediaBean = MediaBean()
                    mediaBean.uri = (
                            ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                cursor.getInt(idIndex).toLong()
                            )
                            )
                    val videoPath = cursor.getString(pathIndex)
                    if (checkVideoError(videoPath)) {
                        continue
                    }
                    mediaBean.path = (videoPath)
                    mediaBean.date = (cursor.getLong(dateIndex))
                    mediaBean.type = (MediaType.TYPE_VIDEO)

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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        result
    }

    suspend fun getAudioMedia(
        context: Context,
        path: String,
        maxDuration: Long = 0
    ) = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        val result: MutableList<MediaBean> = mutableListOf()
        var cursor: Cursor? = null
        try {
            cursor = if (TextUtils.isEmpty(path)) {
                contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Audio.AudioColumns._ID,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DATE_ADDED,
                        MediaStore.Audio.AudioColumns.DURATION,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM
                    ),
                    null,
                    null,
                    MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC" + ", " + MediaStore.Audio.AudioColumns._ID + " ASC"
                )
            } else {
                contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Audio.AudioColumns._ID,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DATE_ADDED,
                        MediaStore.Audio.AudioColumns.DURATION,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM
                    ),
                    MediaStore.Audio.AudioColumns.DATA + " like ? ",
                    arrayOf(path + File.separator + "%"),
                    MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC" + ", " + MediaStore.Audio.AudioColumns._ID + " ASC"
                )
            }
            if (cursor == null) {
                return@withContext result
            }
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)
                val pathIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                val dateIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED)
                val durationIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
                val albumIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)
                do {
                    val mediaBean = MediaBean()
                    mediaBean.uri = (
                            ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                cursor.getInt(idIndex).toLong()
                            )
                            )
                    val audioPath = cursor.getString(pathIndex)
                    if (checkAudioError(audioPath)) {
                        continue
                    }
                    mediaBean.path = (audioPath)
                    mediaBean.date = (cursor.getLong(dateIndex))
                    mediaBean.type = (MediaType.TYPE_AUDIO)
                    mediaBean.musicTitle = cursor.getString(titleIndex)
                    mediaBean.musicArtist = cursor.getString(artistIndex)
                    mediaBean.musicAlbum = cursor.getString(albumIndex)

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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        result
    }

    suspend fun getAllMediaBeanFromBucketId(
        context: Context,
        bucketId: String?
    ) = withContext(Dispatchers.IO) {
        return@withContext getAllMediaBeanFromBucketId(context, bucketId, -1)
    }

    /**
     * 获取某个文件夹下的所有图片和视频的URI
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
        val result1: ArrayList<MediaBean> = getImageMediaBeanFromBucketId(context, bucketId)
        val result2: ArrayList<MediaBean> = getVideoMediaBeanFromBucketId(context, bucketId)
        val result3: ArrayList<MediaBean> = getAudioMediaBeanFromBucketId(context, bucketId)
        val result4 = doMediaBeanAlgorithm(result1, result2)
        return@withContext doMediaBeanAlgorithm(result4, result3)
    }

    suspend fun getImageMediaBeanFromBucketId(
        context: Context,
        bucketId: String?
    ) = withContext(Dispatchers.IO) {
        return@withContext getImageMediaBeanFromBucketId(context, bucketId, true)
    }

    /**
     * 获取某个文件夹下的所有图片的URI
     *
     * @param context
     * @param bucketId
     * @return
     */
    suspend fun getImageMediaBeanFromBucketId(
        context: Context,
        bucketId: String?,
        includeGif: Boolean
    ) = withContext(Dispatchers.IO) {
        val result1: ArrayList<MediaBean> = ArrayList<MediaBean>()
        if (TextUtils.isEmpty(bucketId)) {
            return@withContext result1
        }
        var c1: Cursor? = null
        //Images.ImageColumns.DATA 是图片的绝对路径
        try {
            val cr = context.contentResolver
            c1 = cr.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.ORIENTATION
                ),
                MediaStore.Images.ImageColumns.BUCKET_ID + " = ? ",
                arrayOf(bucketId),
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Images.ImageColumns._ID + " ASC"
            )
            if (c1 == null) {
                return@withContext result1
            }
            if (c1.moveToFirst()) {
                val idIndex = c1.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                val pathIndex = c1.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                val dateIndex = c1.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
                val degreeIndex = c1.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION)
                do {
                    val bb = MediaBean()
                    bb.uri = (
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            c1.getInt(idIndex).toLong()
                        )
                    )
                    val thumbPath = c1.getString(pathIndex)
                    if (checkImageError(thumbPath)) {
                        continue
                    }
                    bb.path = (thumbPath)
                    bb.date = (c1.getLong(dateIndex))
                    //                    bb.setDegree(c1.getInt(degreeIndex));
                    if (MediaFile.isGifFileType(bb.path)) {
                        if (!includeGif) {
                            continue
                        }
                        bb.type = (MediaType.TYPE_GIF)
                    } else if (MediaFile.isJPGFileType(bb.path)) {
                        bb.type = (MediaType.TYPE_JPG)
                    } else if (MediaFile.isPNGFileType(bb.path)) {
                        bb.type = (MediaType.TYPE_PNG)
                    } else {
                        bb.type = (MediaType.TYPE_OTHER_IMAGE)
                    }
                    result1.add(bb)
                } while (c1.moveToNext())
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            c1?.close()
        }
        return@withContext result1
    }

    /**
     * 获取某个文件夹下视频的URI
     *
     * @param context
     * @param bucketId
     * @return
     */
    suspend fun getVideoMediaBeanFromBucketId(
        context: Context,
        bucketId: String?
    ) = withContext(Dispatchers.IO){
        val result2: ArrayList<MediaBean> = ArrayList<MediaBean>()
        if (TextUtils.isEmpty(bucketId)) {
            return@withContext result2
        }
        var c2: Cursor? = null
        //Images.ImageColumns.DATA 是图片的绝对路径
        try {
            val cr = context.contentResolver
            c2 = cr.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Video.VideoColumns._ID,
                    MediaStore.Video.VideoColumns.DATA,
                    MediaStore.Video.VideoColumns.DATE_TAKEN,
                    MediaStore.Video.VideoColumns.DURATION
                ),
                MediaStore.Video.VideoColumns.BUCKET_ID + " = ? ",
                arrayOf(bucketId),
                MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Video.VideoColumns._ID + " ASC"
            )
            if (c2 == null) {
                return@withContext result2
            }
            if (c2.moveToFirst()) {
                val idIndex = c2.getColumnIndex(MediaStore.Video.VideoColumns._ID)
                val pathIndex = c2.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
                val dateIndex = c2.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN)
                val durationIndex = c2.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                do {
                    val bb = MediaBean()
                    bb.uri = (
                        ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            c2.getInt(idIndex).toLong()
                        )
                    )
                    val videoPath = c2.getString(pathIndex)
                    if (checkVideoError(videoPath)) {
                        logE(
                            TAG,
                            "error video path = $videoPath"
                        )
                        continue
                    }
                    bb.path = (videoPath)

                    //有些文件后缀为视频格式，却不是视频文件，长度为0， 需要排除
                    val time = c2.getLong(durationIndex)
                    if (time <= 0) {
                        continue
                    }
                    bb.duration = (time)
                    bb.date = (c2.getLong(dateIndex))
                    bb.type = (MediaType.VIDEO)
                    result2.add(bb)
                } while (c2.moveToNext())
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            c2?.close()
        }
        return@withContext result2
    }

    /**
     * 获取某个文件夹下视频的URI
     *
     * @param context
     * @param bucketId
     * @return
     */
    suspend fun getAudioMediaBeanFromBucketId(
        context: Context,
        bucketId: String?
    ) = withContext(Dispatchers.IO){
        val result2: ArrayList<MediaBean> = ArrayList<MediaBean>()
        if (TextUtils.isEmpty(bucketId)) {
            return@withContext result2
        }
        var c2: Cursor? = null
        //Images.ImageColumns.DATA 是图片的绝对路径
        try {
            val cr = context.contentResolver
            c2 = cr.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Audio.AudioColumns._ID,
                    MediaStore.Audio.AudioColumns.DATA,
                    MediaStore.Audio.AudioColumns.DATE_ADDED,
                    MediaStore.Audio.AudioColumns.DURATION,
                    MediaStore.Audio.AudioColumns.TITLE,
                    MediaStore.Audio.AudioColumns.ARTIST,
                    MediaStore.Audio.AudioColumns.ALBUM
                ),
                MediaStore.Audio.AudioColumns.BUCKET_ID + " = ? ",
                arrayOf(bucketId),
                MediaStore.Audio.AudioColumns.DATE_TAKEN + " DESC" + ", " + MediaStore.Audio.AudioColumns._ID + " ASC"
            )
            if (c2 == null) {
                return@withContext result2
            }
            if (c2.moveToFirst()) {
                val idIndex = c2.getColumnIndex(MediaStore.Audio.AudioColumns._ID)
                val pathIndex = c2.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                val dateIndex = c2.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED)
                val durationIndex = c2.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
                val titleIndex = c2.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
                val artistIndex = c2.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
                val albumIndex = c2.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)
                do {
                    val bb = MediaBean()
                    bb.uri = (
                            ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                c2.getInt(idIndex).toLong()
                            )
                            )
                    val audioPath = c2.getString(pathIndex)
                    if (checkAudioError(audioPath)) {
                        logE(
                            TAG,
                            "error audio path = $audioPath"
                        )
                        continue
                    }
                    bb.path = (audioPath)

                    //有些文件后缀为视频格式，却不是视频文件，长度为0， 需要排除
                    val time = c2.getLong(durationIndex)
                    if (time <= 0) {
                        continue
                    }
                    bb.duration = (time)
                    bb.date = (c2.getLong(dateIndex))
                    bb.type = (MediaType.TYPE_AUDIO)
                    bb.musicTitle = c2.getString(titleIndex)
                    bb.musicArtist = c2.getString(artistIndex)
                    bb.musicAlbum = c2.getString(albumIndex)
                    result2.add(bb)
                } while (c2.moveToNext())
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            c2?.close()
        }
        return@withContext result2
    }


    /***
     *
     * @param path
     * @return error -> true
     */
    private fun checkImageError(path: String): Boolean {
        return if (!FileUtils.isExistsFile(path)) {
            true
        } else !MediaFile.isImageFile(path)
    }

    private fun checkVideoError(path: String): Boolean {
        return if (!FileUtils.isExistsFile(path)) {
            true
        } else !MediaFile.isVideoFile(path)
    }

    private fun checkAudioError(path: String): Boolean {
        return if (!FileUtils.isExistsFile(path)) {
            true
        } else !MediaFile.isAudioFileType(path)
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

}