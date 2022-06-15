package app.allever.android.lib.core.function.imageloader.coil

import android.graphics.*
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

class BorderCircleTransformation(
    private val borderWidth: Int = 0,
    private val borderColor: Int = Color.parseColor("#00000000")
) : Transformation {

    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val minSize = min(input.width, input.height)
        val radius = minSize / 2f
        val output = createBitmap(minSize, minSize)
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, paint)
            if (borderWidth > 0) {
                paint.xfermode = null
                paint.color = borderColor
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = borderWidth.toFloat()
                paint.isAntiAlias = true
                drawCircle(radius, radius, radius - borderWidth / 2f, paint)
            }
        }

        return output
    }

    override fun equals(other: Any?) = other is BorderCircleTransformation

    override fun hashCode() = javaClass.hashCode()
}