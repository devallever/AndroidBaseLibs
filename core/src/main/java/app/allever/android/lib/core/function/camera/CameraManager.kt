package app.allever.android.lib.core.function.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.camera2.CameraManager
import android.view.View
import androidx.lifecycle.LifecycleOwner
import java.io.*

@SuppressLint("StaticFieldLeak")
object CameraManager : ICameraProxy {

    private var cameraProxy: ICameraProxy? = null
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun injectProxy(cameraProxy: ICameraProxy) {
        this.cameraProxy = cameraProxy
    }

    override fun openCamera() {
        cameraProxy?.openCamera()
    }

    override fun openCamera(@CameraFacing.STATE cameraFacing: Int) {
        cameraProxy?.openCamera(cameraFacing)
    }

    override fun closeCamera() {
        cameraProxy?.closeCamera()
    }

    override fun release() {
        cameraProxy?.release()
    }

    override fun setPreview(view: View) {
        cameraProxy?.setPreview(view)
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
        cameraProxy?.setLifeCycleOwner(lifecycleOwner)
    }

    override fun takePicture() {
        cameraProxy?.takePicture()
    }

    override fun setCameraListener(listener: CameraListener?) {
        cameraProxy?.setCameraListener(listener)
    }

    override fun getDisplayOrientation(context: Context?, cameraId: Int): Int {
        return cameraProxy?.getDisplayOrientation(context, cameraId) ?: 0
    }

    override fun getSupportPreviewSize(): MutableList<Size> =
        cameraProxy?.getSupportPreviewSize() ?: mutableListOf()

    override fun getSupportPictureSize(): MutableList<Size> =
        cameraProxy?.getSupportPictureSize() ?: mutableListOf()


    fun getCameraCount(context: Context?): Int {
        val cameraService = context?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        return cameraService?.cameraIdList?.size ?: 0
    }

    fun getCameraIdList(context: Context?): MutableList<Int> {
        val cameraService = context?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        val idArray = cameraService?.cameraIdList
        val idList = mutableListOf<Int>()
        idArray?.forEach {
            idList.add(it.toInt())
        }
        return idList
    }

    /**
     * 获取旋转后的图片
     * @param b
     * @param rotateDegree 旋转角度
     * @return
     */
    fun getRotateBitmap(b: Bitmap?, rotateDegree: Float): Bitmap? {
        if (b == null) {
            return null
        }
        val matrix = Matrix()
        matrix.postRotate(rotateDegree)
        return Bitmap.createBitmap(b, 0, 0, b.width, b.height, matrix, false)
    }

    fun saveBitmap2File(bitmap: Bitmap?, filePath: String?): Boolean {
        if (bitmap == null) {
            return false
        }
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val bytes = baos.toByteArray()
        return saveByteToSDFile(bytes, filePath)
    }


    /**
     * 保存数据到指定文件
     *
     * @param byteData
     * @param filePathName
     * @return true for save successful, false for save failed.
     */
    private fun saveByteToSDFile(byteData: ByteArray?, filePathName: String?): Boolean {
        var result = false
        try {
            val newFile: File? = createNewFile(filePathName, false)
            val fileOutputStream = FileOutputStream(newFile)
            fileOutputStream.write(byteData)
            fileOutputStream.flush()
            fileOutputStream.close()
            result = true
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: SecurityException) {
            // TODO: handle exception
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }
        return result
    }

    /**
     * @param path：文件路径
     * @param append：若存在是否插入原文件
     * @return
     */
    private fun createNewFile(path: String?, append: Boolean): File? {
        val newFile = File(path)
        if (!append) {
            if (newFile.exists()) {
                newFile.delete()
            }
        }
        if (!newFile.exists()) {
            try {
                val parent = newFile.parentFile
                if (parent != null && !parent.exists()) {
                    parent.mkdirs()
                }
                newFile.createNewFile()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return newFile
    }
}