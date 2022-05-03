package app.allever.android.lib.core.helper

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import java.io.*

object FileHelper {

    fun getFileName(path: String?): String {
        var fileName = "Unknown"
        val array = path?.split("/")
        if (array?.isNotEmpty() == true){
            fileName = array[array.size - 1]
        }
        return fileName
    }

    /**
     * 检查文件是否存在
     *
     * @param path 文件的路径
     * @return 文件是否存在
     */
    fun checkExist(path: String?): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }

        return try {
            val file = File(path)
            file.exists()
        } catch (e: Exception) {
            false
        }
    }

    /***
     * 读取文本文件
     * @param path 路径
     * @return 文本内容
     */
    fun readTextFile(path: String?): String {
        var result = ""

        if (!checkExist(path)) return result

        try {
            result = readTextFileFromInputStream(FileInputStream(path))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    /***
     * 读取assets目录的文本文件
     * @param path 路径
     * @return 文本内容
     */
    fun readAssetsTextFile(path: String?): String {
        var result = ""

        if (checkExist(path)) return result

        try {
            val assetManager = App.context.assets
            //path不可能为null
            result = readTextFileFromInputStream(assetManager.open(path!!))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun readTextFileFromInputStream(inputStream: InputStream): String {
        var result = ""
        val bufferedInputStream = BufferedInputStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            var len = -1
            val buffer = ByteArray(1024)
            while (bufferedInputStream.read(buffer).also { len = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, len)
            }
            val dataArray = byteArrayOutputStream.toByteArray()
            result = String(dataArray)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            bufferedInputStream.close()
            byteArrayOutputStream.close()
            inputStream.close()
        }

        return result
    }

    /**
     * 根据URI获取文件真实路径（兼容多张机型）
     * @param context
     * @param uri
     * @return
     * @参考 android从相册选择图片和拍照选择图片
     * @linl https://www.jianshu.com/p/f13dfd5a823e
     */
    fun getFilePathByUri(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return ""
        }

        if ("content".equals(uri.scheme, ignoreCase = true)) {

            val sdkVersion = Build.VERSION.SDK_INT
            return if (sdkVersion >= 19) { // api >= 19
                getRealPathFromUriAboveApi19(context, uri)
            } else { // api < 19
                getRealPathFromUriBelowAPI19(context, uri)
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            val documentId = DocumentsContract.getDocumentId(uri)
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                val type =
                    documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                val id =
                    documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(id)

                //
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                filePath = getDataColumn(context, contentUri, selection, selectionArgs)
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId)
                )
                filePath = getDataColumn(context, contentUri, null, null)
            } else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    filePath = Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else {
                //Log.e("路径错误");
                log("路径错误")
            }
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null)
        } else if ("file" == uri.scheme) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.path
        }
        return filePath
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri, null, null)
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var path: String? = null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                path = cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            cursor?.close()
        }

        return path
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }
}