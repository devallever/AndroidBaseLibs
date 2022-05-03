package app.allever.android.lib.core.helper

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import app.allever.android.lib.core.app.App

import java.io.IOException
import java.io.InputStream
import java.util.*

object AssetsHelper {
    private val mAssetsRes = WeakHashMap<String, Any>()

    fun toInputStream(path: String?): InputStream? {
        if (path != null) {
            try {
                return App.context.assets.open(path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun toBitmap(imgPath: String?): Bitmap? {
        if (imgPath != null) {
            val key = imgPath + "_bitmap"
            val bitmap = mAssetsRes[key]
            if (bitmap is Bitmap && !bitmap.isRecycled) {
                return bitmap
            }
            val inputStream = toInputStream(imgPath)
            if (inputStream != null) {
                val b = BitmapFactory.decodeStream(inputStream)
                if (b != null) {
                    mAssetsRes[key] = b
                }
                return b
            }
        }
        return null
    }

    fun toBitmap(resource: Resources, packageName: String, resId: Int): Bitmap? {
        if (resId == 0) {
            return null
        }

        val key = "${packageName}_${resId}_bitmap"
        val bitmap = mAssetsRes[key]
        if (bitmap is Bitmap && !bitmap.isRecycled) {
            return bitmap
        }

        val b = BitmapFactory.decodeResource(resource, resId)
        if (b != null) {
            mAssetsRes[key] = b
        }
        return b
    }

    fun toScaleBitmap(imgPath: String?, requestWith: Int, requestHeight: Int): Bitmap? {
        val key = imgPath + "_bitmap_scale"
        val sourceBitmap = toBitmap(imgPath) ?: return null
        return scaleBitmap(
            key,
            sourceBitmap,
            requestWith,
            requestHeight
        )
    }

    fun toScaleBitmap(resource: Resources, packageName: String, resId: Int, requestWith: Int, requestHeight: Int): Bitmap? {
        val key = "${packageName}_${resId}_bitmap_scale"
        val sourceBitmap = toBitmap(
            resource,
            packageName,
            resId
        ) ?: return null
        return scaleBitmap(
            key,
            sourceBitmap,
            requestWith,
            requestHeight
        )
    }


    private fun scaleBitmap(key: String, sourceBitmap: Bitmap, requestWith: Int, requestHeight: Int): Bitmap? {
        var scaleBitmap = mAssetsRes[key]
        if (scaleBitmap is Bitmap && !scaleBitmap.isRecycled) {
            return scaleBitmap
        }

        val bitmapWidth = sourceBitmap.width.toFloat()
        val bitmapHeight = sourceBitmap.height.toFloat()
        val scaleWith = requestWith / bitmapWidth
        val scaleHeight = requestHeight / bitmapHeight

        scaleBitmap = Bitmap.createScaledBitmap(sourceBitmap,
                (bitmapWidth * scaleWith).toInt(),
                (bitmapHeight * scaleHeight).toInt(),
                true)
        if (scaleBitmap != null) {
            mAssetsRes[key] = scaleBitmap
        }
        return scaleBitmap
    }


    fun toDrawable(imgPath: String?): Drawable? {
        if (imgPath != null) {
            val key = imgPath + "_drawable"
            val drawable = mAssetsRes[key]
            if (drawable is Drawable) {
                return drawable
            }
            val inputStream = toInputStream(imgPath)
            if (inputStream != null) {
                val d = Drawable.createFromStream(inputStream, null)
                mAssetsRes[key] = d
                return d
            }
        }
        return null
    }

}
